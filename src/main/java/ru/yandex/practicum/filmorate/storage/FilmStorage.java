package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    Film getFilmById(long id);

    Collection<Film> getAllFilms();

    void addLike(long id, long userId);

    void deleteLike(long id, long userId);

    Collection<Film> getTopNPopularFilms(Integer count);

}
