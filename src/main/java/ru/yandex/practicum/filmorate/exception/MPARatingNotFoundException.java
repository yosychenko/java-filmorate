package ru.yandex.practicum.filmorate.exception;

public class MPARatingNotFoundException extends RuntimeException {
    public MPARatingNotFoundException(long genreId) {
        super(String.format("MPA рейтинг c ID=%s не найден.", genreId));
    }
}
