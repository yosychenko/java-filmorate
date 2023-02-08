package ru.yandex.practicum.filmorate.exception;

public class UserFilmAddLikeException extends RuntimeException {
    public UserFilmAddLikeException(long filmId, long userId) {
        super(String.format("Невозможно поставить лайк фильму с ID=%s от пользователя с ID=%s. " +
                "Возможно, указанные фильм или пользователь не существуют.", filmId, userId));
    }
}
