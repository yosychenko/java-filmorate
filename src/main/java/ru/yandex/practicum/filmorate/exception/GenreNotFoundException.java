package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(long genreId) {
        super(String.format("Жанр c ID=%s не найден.", genreId));
    }
}
