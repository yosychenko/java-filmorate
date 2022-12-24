package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film> {
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        validate(newFilm);
        newFilm.setId(++idCounter);
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        validate(newFilm);
        Film existingFilm = films.get(newFilm.getId());
        if (existingFilm != null) {
            films.put(newFilm.getId(), newFilm);
            return newFilm;
        }
        throw new FilmNotFoundException(String.format("Фильм c ID=%s не найден.", newFilm.getId()));
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private void validate(Film film) {
        final LocalDate firstMovieReleaseDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(firstMovieReleaseDate)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
    }
}
