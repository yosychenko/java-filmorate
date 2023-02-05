package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FilmMapper implements RowMapper<Film> {

    private final static String GENRES_SEPARATOR = ",";

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .mpa(
                        MPARating.builder()
                                .id(rs.getLong("mpa_rating_id"))
                                .name(rs.getString("mpa_rating_name"))
                                .build()
                )
                .genres(mapGenres(rs.getString("genres_ids"), rs.getString("genres_names")))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .build();
    }

    private List<Genre> mapGenres(String genresIdsString, String genresNames) {
        if (genresIdsString != null) {
            String[] parsedGenresIds = genresIdsString.split(GENRES_SEPARATOR);
            String[] parsedGenresNames = genresNames.split(GENRES_SEPARATOR);

            return IntStream.range(0, parsedGenresIds.length)
                    .mapToObj(
                            i -> Genre.builder()
                                    .id(Long.parseLong(parsedGenresIds[i]))
                                    .name(parsedGenresNames[i])
                                    .build()
                    )
                    .collect(Collectors.toList());
        }
        return List.of();
    }

}
