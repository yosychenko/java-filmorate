package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 0;

    @Override
    public Film createFilm(Film newFilm) {
        newFilm.setId(++idCounter);
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        Film existingFilm = films.get(newFilm.getId());
        if (existingFilm != null) {
            films.put(newFilm.getId(), newFilm);
            return newFilm;
        }
        throw new FilmNotFoundException(newFilm.getId());
    }

    @Override
    public Film getFilmById(long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new FilmNotFoundException(id);
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
