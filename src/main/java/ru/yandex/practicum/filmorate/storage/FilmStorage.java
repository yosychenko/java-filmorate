package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film createFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    Film getFilmById(long id);

    Collection<Film> getAllFilms();
}
