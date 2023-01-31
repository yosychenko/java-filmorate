package ru.yandex.practicum.filmorate.mapper;


import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MPARatingMapper implements RowMapper<MPARating> {
    @Override
    public MPARating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MPARating.builder()
                .id(rs.getLong("id"))
                .rating(rs.getString("rating"))
                .build();
    }
}
