package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(long filmId) {
        super(String.format("Фильм c ID=%s не найден.", filmId));
    }
}
