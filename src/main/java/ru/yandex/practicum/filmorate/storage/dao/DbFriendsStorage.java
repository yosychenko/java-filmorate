package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserDeleteFriendException;
import ru.yandex.practicum.filmorate.exception.UserUpdateFriendException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.Collection;

@Component
public class DbFriendsStorage implements FriendsStorage {

    private final static int NOT_ACCEPTED_STATUS_ID = 2;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbFriendsStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
