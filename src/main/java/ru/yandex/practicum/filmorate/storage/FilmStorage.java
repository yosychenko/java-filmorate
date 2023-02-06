package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    Film getFilmById(long id);

    Collection<Film> getAllFilms();

    Collection<Film> getTopNPopularFilms(Integer count);

}
