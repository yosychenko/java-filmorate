package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MPARatingNotFoundException;
import ru.yandex.practicum.filmorate.mapper.MPARatingMapper;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPARatingStorage;

import java.util.List;

@Component
public class DbMPARatingStorage implements MPARatingStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbMPARatingStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPARating> getAllMPARatings() {
        String sql = "SELECT id, rating FROM dict_mpa_rating";

        return jdbcTemplate.query(sql, new MPARatingMapper());
    }

    @Override
    public MPARating getMPARatingById(long id) {
        String sql = "SELECT id, rating FROM dict_mpa_rating WHERE id  = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new MPARatingMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new MPARatingNotFoundException(id);
        }
    }
}
