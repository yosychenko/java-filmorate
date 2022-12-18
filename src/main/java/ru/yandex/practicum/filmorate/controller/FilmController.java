package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter = 0;

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        newFilm.setId(++idCounter);
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        Film existingFilm = films.get(newFilm.getId());
        if (existingFilm != null) {
            films.put(newFilm.getId(), newFilm);
            return newFilm;
        }
        throw new FilmNotFoundException();
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
