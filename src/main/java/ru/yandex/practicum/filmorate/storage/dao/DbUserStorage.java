package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Map;

@Component
public class DbUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User newUser) {
        // Вставим пользователя в таблицу users
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        long generatedUserId = simpleJdbcInsert.executeAndReturnKey(
                Map.of(
                        "email", newUser.getEmail(),
                        "login", newUser.getLogin(),
                        "name", newUser.getName(),
                        "birthday", newUser.getBirthday()
                )
        ).longValue();
        newUser.setId(generatedUserId);

        return getUserById(newUser.getId());
    }

    @Override
    public User updateUser(User newUser) {
        // Обновим пользователя в таблице users
        int rowsAffected = jdbcTemplate.update(
                "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?",
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId()
        );

        if (rowsAffected == 0) {
            throw new UserNotFoundException(newUser.getId());
        }

        return getUserById(newUser.getId());
    }

    @Override
    public User getUserById(long id) {
        // Получим пользователя по его ID
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, email, login, name, birthday FROM users WHERE id = ?",
                    new UserMapper(),
                    id
            );
        } catch (EmptyResultDataAccessException exception) {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public List<User> getAllUsers() {
        // Получим всех пользователей
        return jdbcTemplate.query(
                "SELECT id, email, login, name, birthday FROM users",
                new UserMapper()
        );
    }
}
