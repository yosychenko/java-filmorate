package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.MPARatingNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

@Component
public class DbFilmStorage implements FilmStorage {

    private final static int TOP_N_DEFAULT_VALUE = 10;
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
                .usingGeneratedKeyColumns("id")
                .usingColumns("mpa_rating_id", "name", "description", "release_date", "duration");

        try {
            long generatedFilmId = simpleJdbcInsert.executeAndReturnKey(
                    Map.of(
                            "mpa_rating_id", newFilm.getMpa().getId(),
                            "name", newFilm.getName(),
                            "description", newFilm.getDescription(),
                            "release_date", newFilm.getReleaseDate(),
                            "duration", newFilm.getDuration()
                    )
            ).longValue();
            newFilm.setId(generatedFilmId);
        } catch (DataAccessException ex) {
            throw new MPARatingNotFoundException(newFilm.getMpa().getId());
        }

        if (newFilm.getGenres() != null) {
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
        }

        return getFilmById(newFilm.getId());
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

        try {
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
        } catch (DataAccessException ex) {
            throw new MPARatingNotFoundException(newFilm.getMpa().getId());
        }

        if (newFilm.getGenres() != null) {
            // Обновим жанры указанного фильма

            // Удалим существующие жанры у указанного фильма
            jdbcTemplate.update(
                    "DELETE FROM mtm_film_genre WHERE film_id = ?",
                    newFilm.getId()
            );

            // Если на обновление пришел не пустой список жанров - обновим их значениями из списка
            if (newFilm.getGenres().size() > 0) {
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
            }
        }

        return getFilmById(newFilm.getId());
    }

    @Override
    public Film getFilmById(long id) {
        // Получим фильм по указанному ID
        String sql = "SELECT f.id," +
                "       f.mpa_rating_id," +
                "       mpa_rating.name AS mpa_rating_name," +
                "       fg.genres_ids," +
                "       fg.genres_names," +
                "       f.name," +
                "       f.description," +
                "       f.release_date," +
                "       f.duration " +
                "FROM film f " +
                "         LEFT JOIN (" +
                "            SELECT mtm_fg.film_id," +
                "                   listagg(mtm_fg.genre_id, ',') WITHIN GROUP (ORDER BY mtm_fg.genre_id) AS genres_ids," +
                "                   listagg(genre.name, ',') WITHIN GROUP (ORDER BY mtm_fg.genre_id) AS genres_names " +
                "            FROM mtm_film_genre mtm_fg " +
                "            JOIN dict_genre AS genre ON mtm_fg.genre_id = genre.id " +
                "            GROUP BY mtm_fg.film_id" +
                "         ) AS fg ON f.id = fg.film_id " +
                "         LEFT JOIN dict_mpa_rating AS mpa_rating ON f.mpa_rating_id = mpa_rating.id " +
                "WHERE f.id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new FilmMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new FilmNotFoundException(id);
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        // Получим все фильмы
        String sql = "SELECT f.id," +
                "       f.mpa_rating_id," +
                "       mpa_rating.name AS mpa_rating_name," +
                "       fg.genres_ids," +
                "       fg.genres_names," +
                "       f.name," +
                "       f.description," +
                "       f.release_date," +
                "       f.duration " +
                "FROM film f " +
                "         LEFT JOIN (" +
                "            SELECT mtm_fg.film_id," +
                "                   listagg(mtm_fg.genre_id, ',') WITHIN GROUP (ORDER BY mtm_fg.genre_id) AS genres_ids," +
                "                   listagg(genre.name, ',') WITHIN GROUP (ORDER BY mtm_fg.genre_id) AS genres_names " +
                "            FROM mtm_film_genre mtm_fg " +
                "            JOIN dict_genre AS genre ON mtm_fg.genre_id = genre.id " +
                "            GROUP BY mtm_fg.film_id " +
                "         ) AS fg ON f.id = fg.film_id " +
                "         LEFT JOIN dict_mpa_rating AS mpa_rating ON f.mpa_rating_id = mpa_rating.id";

        return jdbcTemplate.query(sql, new FilmMapper());
    }

    @Override
    public Collection<Film> getTopNPopularFilms(Integer count) {
        // Получим топ N популярных фильмов
        String sql = "SELECT f.id," +
                "       f.mpa_rating_id," +
                "       mpa_rating.name AS mpa_rating_name," +
                "       fg.genres_ids," +
                "       fg.genres_names," +
                "       f.name," +
                "       f.description," +
                "       f.release_date," +
                "       f.duration " +
                "FROM film f " +
                "         LEFT JOIN (" +
                "            SELECT mtm_fg.film_id," +
                "                   listagg(mtm_fg.genre_id, ',') WITHIN GROUP (ORDER BY mtm_fg.genre_id) AS genres_ids," +
                "                   listagg(genre.name, ',') WITHIN GROUP (ORDER BY mtm_fg.genre_id) AS genres_names " +
                "            FROM mtm_film_genre mtm_fg " +
                "            JOIN dict_genre AS genre ON mtm_fg.genre_id = genre.id " +
                "            GROUP BY mtm_fg.film_id " +
                "         ) AS fg ON f.id = fg.film_id " +
                "         LEFT JOIN dict_mpa_rating AS mpa_rating ON f.mpa_rating_id = mpa_rating.id " +
                "ORDER BY f.rate DESC " +
                "LIMIT ?;";

        return jdbcTemplate.query(sql, new FilmMapper(), count != null ? count : TOP_N_DEFAULT_VALUE);
    }

}
