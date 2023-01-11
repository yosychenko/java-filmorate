package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
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
    public List<Film> getAllFilms() {
        return super.getAllRecords();
    }
}
