package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage extends AbstractStorage<Film> implements FilmStorage {

    @Override
    public Film createFilm(Film newFilm) {
        newFilm.setId(++super.idCounter);
        return super.createRecord(newFilm.getId(), newFilm);
    }

    @Override
    public Film updateFilm(Film newFilm) {
        return super.updateRecord(newFilm.getId(), newFilm, new FilmNotFoundException(newFilm.getId()));
    }

    @Override
    public Film getFilmById(long id) {
        return super.getRecordById(id, new FilmNotFoundException(id));
    }

    @Override
    public Collection<Film> getAllFilms() {
        return super.getAllRecords();
    }
}
