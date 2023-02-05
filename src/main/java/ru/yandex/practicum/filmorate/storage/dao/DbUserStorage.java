package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserDeleteFriendException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserUpdateFriendException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class DbUserStorage implements UserStorage {

    private final static int NOT_ACCEPTED_STATUS_ID = 2;

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

    @Override
    public void addFriend(long id, long friendId) {
        // Добавим друга пользователю
        try {
            jdbcTemplate.update(
                    "MERGE INTO mtm_user_user_friendship KEY(user_1_id, user_2_id) VALUES(?, ?, ?)",
                    id,
                    friendId,
                    NOT_ACCEPTED_STATUS_ID
            );
        } catch (DataAccessException ex) {
            throw new UserUpdateFriendException(id, friendId);
        }
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        // Удалим друга у пользователя
        int rowsAffected = jdbcTemplate.update(
                "DELETE FROM mtm_user_user_friendship WHERE user_1_id = ? AND user_2_id = ?",
                id,
                friendId
        );

        if (rowsAffected == 0) {
            throw new UserDeleteFriendException(id, friendId);
        }
    }

    @Override
    public Collection<User> getFriends(long id) {
        // Получим всех друзей пользователя
        String sql = "SELECT u.id," +
                "       u.email," +
                "       u.login," +
                "       u.name," +
                "       u.birthday " +
                "FROM users u " +
                "JOIN mtm_user_user_friendship friends ON u.id = friends.user_2_id " +
                "WHERE friends.user_1_id = ? ";

        return jdbcTemplate.query(sql, new UserMapper(), id);
    }

    @Override
    public Collection<User> getCommonFriends(long id, long otherId) {
        String sql = "SELECT u.id," +
                "       u.email," +
                "       u.login," +
                "       u.name," +
                "       u.birthday " +
                "FROM users u " +
                "JOIN (" +
                "    SELECT user_2_id AS friend_id " +
                "    FROM mtm_user_user_friendship " +
                "    WHERE user_1_id = ? " +
                "    INTERSECT " +
                "    SELECT user_2_id AS friend_id " +
                "    FROM mtm_user_user_friendship " +
                "    WHERE user_1_id = ? " +
                ") common_friends ON u.id = common_friends.friend_id;";

        return jdbcTemplate.query(sql, new UserMapper(), id, otherId);
    }
}
