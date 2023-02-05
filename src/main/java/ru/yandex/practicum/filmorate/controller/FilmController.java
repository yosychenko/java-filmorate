package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        validate(newFilm);
        return filmStorage.createFilm(newFilm);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        validate(newFilm);
        return filmStorage.updateFilm(newFilm);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmStorage.getFilmById(id);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmStorage.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmStorage.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopNPopularFilms(@RequestParam(required = false) Integer count) {
        return filmStorage.getTopNPopularFilms(count);
    }

    private void validate(Film film) {
        final LocalDate firstMovieReleaseDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(firstMovieReleaseDate)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
    }
}
