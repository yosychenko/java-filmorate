package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

@Component("DbFilmStorage")
public class DbFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film newFilm) {
        // Вставим фильм в таблицу film
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("id");
        long generatedID = simpleJdbcInsert.executeAndReturnKey(
                Map.of(
                        "mpa_rating_id", newFilm.getMpa().getId(),
                        "name", newFilm.getName(),
                        "description", newFilm.getDescription(),
                        "release_date", newFilm.getReleaseDate(),
                        "duration", newFilm.getDuration()
                )
        ).longValue();
        newFilm.setId(generatedID);

        // Вставим его жанры в таблицу mtm_film_genre
        jdbcTemplate.batchUpdate(
                "INSERT INTO mtm_film_genre (film_id, genre_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, newFilm.getId());
                        ps.setLong(2, newFilm.getGenres().get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return newFilm.getGenres().size();
                    }
                });

        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        // Обновим фильм в таблице film
        String sql = "UPDATE film SET " +
                "mpa_rating_id = ?, " +
                "name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ? " +
                "WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(
                sql,
                newFilm.getMpa().getId(),
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getId()
        );

        if (rowsAffected == 0) {
            throw new FilmNotFoundException(newFilm.getId());
        }

        // Обновим его жанры в таблице
        jdbcTemplate.batchUpdate(
                "MERGE INTO mtm_film_genre KEY(film_id, genre_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, newFilm.getId());
                        ps.setLong(2, newFilm.getGenres().get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return newFilm.getGenres().size();
                    }
                }
        );

        return newFilm;
    }

    @Override
    public Film getFilmById(long id) {
        String sql = "SELECT f.id," +
                "       f.mpa_rating_id," +
                "       fg.genres," +
                "       f.name," +
                "       f.description," +
                "       f.release_date," +
                "       f.duration " +
                "FROM film f " +
                "JOIN (" +
                "    SELECT film_id," +
                "           listagg(genre_id, ',') WITHIN GROUP (ORDER BY genre_id) AS genres" +
                "    FROM mtm_film_genre " +
                "    GROUP BY film_id " +
                ") fg ON f.id = fg.film_id " +
                "WHERE f.id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new FilmMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new FilmNotFoundException(id);
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT f.id," +
                "       f.mpa_rating_id," +
                "       fg.genres," +
                "       f.name," +
                "       f.description," +
                "       f.release_date," +
                "       f.duration " +
                "FROM film f " +
                "JOIN (" +
                "    SELECT film_id," +
                "           listagg(genre_id, ',') WITHIN GROUP (ORDER BY genre_id) AS genres" +
                "    FROM mtm_film_genre " +
                "    GROUP BY film_id " +
                ") fg ON f.id = fg.film_id";

        return jdbcTemplate.query(sql, new FilmMapper());
    }
}
