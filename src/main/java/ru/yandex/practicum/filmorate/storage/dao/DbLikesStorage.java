package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserFilmAddLikeException;
import ru.yandex.practicum.filmorate.exception.UserFilmDeleteLikeException;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Component
public class DbLikesStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbLikesStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long id, long userId) {
        // Добавим лайк указанному фильму от указанного пользователя
        try {
            jdbcTemplate.update(
                    "MERGE INTO mtm_user_film_likes KEY(user_id, film_id) VALUES (?, ?)",
                    userId,
                    id
            );
        } catch (DataAccessException ex) {
            throw new UserFilmAddLikeException(id, userId);
        }
    }

    @Override
    public void deleteLike(long id, long userId) {
        // Удалим лайк
        int rowsAffected = jdbcTemplate.update(
                "DELETE FROM mtm_user_film_likes WHERE user_id = ? AND film_id = ?",
                userId,
                id
        );

        if (rowsAffected == 0) {
            throw new UserFilmDeleteLikeException(id, userId);
        }
    }
}
