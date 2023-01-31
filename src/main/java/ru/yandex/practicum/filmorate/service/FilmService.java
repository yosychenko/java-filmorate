package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final static int DEFAULT_LIMIT = 10;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(
            @Qualifier("DbFilmStorage") FilmStorage filmStorage,
            @Qualifier("DbUserStorage") UserStorage userStorage
    ) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long id, long userId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(id);

        film.getLikes().add(user.getId());
    }

    public void deleteLike(long id, long userId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(id);

        film.getLikes().remove(user.getId());
    }

    public List<Film> getTopNPopularFilms(Integer count) {
        if (count == null) {
            count = DEFAULT_LIMIT;
        }

        return filmStorage
                .getAllFilms()
                .stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
