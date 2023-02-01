package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FilmMapper implements RowMapper<Film> {

    private final static String GENRES_SEPARATOR = ",";

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .mpa(
                        MPARating.builder()
                                .id(rs.getLong("mpa_rating_id"))
                                .build()
                )
                .genres(
                        Arrays.stream(rs.getString("genres").split(GENRES_SEPARATOR))
                                .map(genre_id -> Genre.builder().id(Long.parseLong(genre_id)).build())
                                .collect(Collectors.toList())
                )
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .build();
    }
}
