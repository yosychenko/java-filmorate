package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.UserFilmAddLikeException;
import ru.yandex.practicum.filmorate.exception.UserFilmDeleteLikeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DbLikesStorageTest {

    private final DbLikesStorage dbLikesStorage;
    private final DbFilmStorage dbFilmStorage;
    private final DbUserStorage dbUserStorage;

    @Test
    public void testAddLikeNonExistentUserAndFilm() {
        assertThatThrownBy(() -> {
            dbLikesStorage.addLike(-9999, -9999);
        })
                .isInstanceOf(UserFilmAddLikeException.class)
                .hasMessageContaining("Невозможно поставить лайк фильму с ID=-9999 от пользователя с ID=-9999. " +
                        "Возможно, указанные фильм или пользователь не существуют.");

    }

    @Test
    public void testDeleteLikeNonExistentUserAndFilm() {
        assertThatThrownBy(() -> {
            dbLikesStorage.deleteLike(-9999, -9999);
        })
                .isInstanceOf(UserFilmDeleteLikeException.class)
                .hasMessageContaining("Невозможно удалить лайк фильму с ID=-9999 от пользователя с ID=-9999. " +
                        "Возможно, указанные фильм или пользователь не существуют.");

    }

    @Test
    public void testAddLikeAndCheckTopNPopularFilms() {
        Film film1 = Film.builder()
                .name("film1")
                .description("description1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).name("G").build())
                .genres(
                        List.of(Genre.builder().id(1).name("Комедия").build())
                )
                .build();
        Film film2 = Film.builder()
                .name("film2")
                .description("description2")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).name("G").build())
                .genres(
                        List.of(Genre.builder().id(1).name("Комедия").build())
                )
                .build();
        User user1 = User.builder()
                .email("user1@example.com")
                .login("login1")
                .name("user1")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        User user2 = User.builder()
                .email("user2@example.com")
                .login("login2")
                .name("user2")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Film createdFilm1 = dbFilmStorage.createFilm(film1);
        Film createdFilm2 = dbFilmStorage.createFilm(film2);
        User createdUser1 = dbUserStorage.createUser(user1);
        User createdUser2 = dbUserStorage.createUser(user2);

        // Два лайка Фильму 2
        dbLikesStorage.addLike(createdFilm2.getId(), createdUser1.getId());
        dbLikesStorage.addLike(createdFilm2.getId(), createdUser2.getId());
        // Один лайк Фильму 1
        dbLikesStorage.addLike(createdFilm1.getId(), createdUser1.getId());

        Collection<Film> likedFilms = dbFilmStorage.getTopNPopularFilms(2);

        assertThat(likedFilms)
                .isNotEmpty()
                .hasSize(2)
                .containsSequence(film2, film1);
    }


    @Test
    public void testDeleteLikeAndCheckTopNPopularFilms() {
        Film film1 = Film.builder()
                .name("film1")
                .description("description1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).name("G").build())
                .genres(
                        List.of(Genre.builder().id(1).name("Комедия").build())
                )
                .build();
        Film film2 = Film.builder()
                .name("film2")
                .description("description2")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).name("G").build())
                .genres(
                        List.of(Genre.builder().id(1).name("Комедия").build())
                )
                .build();
        User user1 = User.builder()
                .email("user1@example.com")
                .login("login1")
                .name("user1")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        User user2 = User.builder()
                .email("user2@example.com")
                .login("login2")
                .name("user2")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Film createdFilm1 = dbFilmStorage.createFilm(film1);
        Film createdFilm2 = dbFilmStorage.createFilm(film2);
        User createdUser1 = dbUserStorage.createUser(user1);
        User createdUser2 = dbUserStorage.createUser(user2);

        // Два лайка Фильму 2
        dbLikesStorage.addLike(createdFilm2.getId(), createdUser1.getId());
        dbLikesStorage.addLike(createdFilm2.getId(), createdUser2.getId());
        // Один лайк Фильму 1
        dbLikesStorage.addLike(createdFilm1.getId(), createdUser1.getId());

        // Удалим все лайки Фильма 2
        dbLikesStorage.deleteLike(createdFilm2.getId(), createdUser1.getId());
        dbLikesStorage.deleteLike(createdFilm2.getId(), createdUser2.getId());

        Collection<Film> likedFilms = dbFilmStorage.getTopNPopularFilms(2);

        assertThat(likedFilms)
                .isNotEmpty()
                .hasSize(2)
                .containsSequence(film1, film2);
    }
}