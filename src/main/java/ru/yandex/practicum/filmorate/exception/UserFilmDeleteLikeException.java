package ru.yandex.practicum.filmorate.exception;

public class UserFilmDeleteLikeException extends RuntimeException {
    public UserFilmDeleteLikeException(long filmId, long userId) {
        super(String.format("Невозможно удалить лайк фильму с ID=%s от пользователя с ID=%s. " +
                "Возможно, указанные фильм или пользователь не существуют.", filmId, userId));
    }
}
