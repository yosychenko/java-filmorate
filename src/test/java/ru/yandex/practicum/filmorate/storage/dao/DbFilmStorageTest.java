package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.MPARatingNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DbFilmStorageTest {

    private final DbFilmStorage dbFilmStorage;

    @Test
    public void testCreateFilmWithoutGenres() {
        Film film = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).build())
                .build();

        Film createdFilm = dbFilmStorage.createFilm(film);

        assertThat(createdFilm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "film")
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 1))
                .hasFieldOrPropertyWithValue("duration", 100)
                .hasFieldOrPropertyWithValue("mpa", MPARating.builder().id(1).name("G").build())
                .hasFieldOrPropertyWithValue("genres", List.of());
    }

    @Test
    public void testCreateFilmWithGenres() {
        Film film = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .genres(
                        List.of(Genre.builder().id(1).name("Комедия").build())
                )
                .mpa(MPARating.builder().id(1).build())
                .build();

        Film createdFilm = dbFilmStorage.createFilm(film);

        assertThat(createdFilm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "film")
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 1))
                .hasFieldOrPropertyWithValue("duration", 100)
                .hasFieldOrPropertyWithValue("mpa", MPARating.builder().id(1).name("G").build())
                .hasFieldOrPropertyWithValue("genres", List.of(Genre.builder().id(1).name("Комедия").build()));
    }

    @Test
    public void testCreateFilmWithNonExistentMPARating() {
        Film film = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .genres(
                        List.of(Genre.builder().id(1).name("Комедия").build())
                )
                .mpa(MPARating.builder().id(-9999).build())
                .build();

        assertThatThrownBy(() -> dbFilmStorage.createFilm(film))
                .isInstanceOf(MPARatingNotFoundException.class)
                .hasMessageContaining("MPA рейтинг c ID=-9999 не найден.");

        assertThatThrownBy(() -> dbFilmStorage.getFilmById(1))
                .isInstanceOf(FilmNotFoundException.class)
                .hasMessageContaining("Фильм c ID=1 не найден.");
    }

    @Test
    public void testUpdateFilmWithoutGenres() {
        Film existingFilm = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).build())
                .build();

        Film newFilm = Film.builder()
                .id(1)
                .name("film_updated")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).build())
                .build();

        dbFilmStorage.createFilm(existingFilm);
        Film updatedFilm = dbFilmStorage.updateFilm(newFilm);

        assertThat(updatedFilm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "film_updated")
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 1))
                .hasFieldOrPropertyWithValue("duration", 100)
                .hasFieldOrPropertyWithValue("mpa", MPARating.builder().id(1).name("G").build())
                .hasFieldOrPropertyWithValue("genres", List.of());
    }

    @Test
    public void testUpdateFilmWithGenresDeleteGenres() {
        Film existingFilm = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).build())
                .genres(
                        List.of(Genre.builder().id(1).name("Комедия").build())
                )
                .build();

        Film newFilm = Film.builder()
                .id(1)
                .name("film_updated")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).build())
                .genres(List.of())
                .build();

        dbFilmStorage.createFilm(existingFilm);
        Film updatedFilm = dbFilmStorage.updateFilm(newFilm);

        assertThat(updatedFilm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "film_updated")
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 1))
                .hasFieldOrPropertyWithValue("duration", 100)
                .hasFieldOrPropertyWithValue("mpa", MPARating.builder().id(1).name("G").build())
                .hasFieldOrPropertyWithValue("genres", List.of());
    }

    @Test
    public void testUpdateFilmWithGenresUpdateGenres() {
        Film existingFilm = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).build())
                .genres(
                        List.of(Genre.builder().id(1).name("Комедия").build())
                )
                .build();

        Film newFilm = Film.builder()
                .id(1)
                .name("film_updated")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).build())
                .genres(
                        List.of(Genre.builder().id(2).name("Драма").build())
                )
                .build();

        dbFilmStorage.createFilm(existingFilm);
        Film updatedFilm = dbFilmStorage.updateFilm(newFilm);

        assertThat(updatedFilm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "film_updated")
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 1))
                .hasFieldOrPropertyWithValue("duration", 100)
                .hasFieldOrPropertyWithValue("mpa", MPARating.builder().id(1).name("G").build())
                .hasFieldOrPropertyWithValue("genres", List.of(Genre.builder().id(2).name("Драма").build()));
    }

    @Test
    public void testUpdateNonExistentFilm() {
        Film existingFilm = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).name("G").build())
                .genres(
                        List.of(Genre.builder().id(1).name("Комедия").build())
                )
                .build();

        Film newFilm = Film.builder()
                .id(-9999)
                .name("film_updated")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).build())
                .build();

        dbFilmStorage.createFilm(existingFilm);

        assertThatThrownBy(() -> {
            dbFilmStorage.updateFilm(newFilm);
        })
                .isInstanceOf(FilmNotFoundException.class)
                .hasMessageContaining("Фильм c ID=-9999 не найден.");

        Film notUpdatedFilm = dbFilmStorage.getFilmById(1);

        assertThat(notUpdatedFilm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "film")
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 1))
                .hasFieldOrPropertyWithValue("duration", 100)
                .hasFieldOrPropertyWithValue("mpa", MPARating.builder().id(1).name("G").build())
                .hasFieldOrPropertyWithValue("genres", List.of(Genre.builder().id(1).name("Комедия").build()));
    }

    @Test
    public void testUpdateFilmNonExistentMPARating() {
        Film existingFilm = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).name("G").build())
                .genres(
                        List.of(Genre.builder().id(1).name("Комедия").build())
                )
                .build();

        Film newFilm = Film.builder()
                .id(1)
                .name("film_updated")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(-9999).build())
                .build();

        dbFilmStorage.createFilm(existingFilm);

        assertThatThrownBy(() -> {
            dbFilmStorage.updateFilm(newFilm);
        })
                .isInstanceOf(MPARatingNotFoundException.class)
                .hasMessageContaining("MPA рейтинг c ID=-9999 не найден.");

        Film notUpdatedFilm = dbFilmStorage.getFilmById(1);

        assertThat(notUpdatedFilm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "film")
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 1))
                .hasFieldOrPropertyWithValue("duration", 100)
                .hasFieldOrPropertyWithValue("mpa", MPARating.builder().id(1).name("G").build())
                .hasFieldOrPropertyWithValue("genres", List.of(Genre.builder().id(1).name("Комедия").build()));
    }

    @Test
    public void testGetFilmById() {
        Film existingFilm = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).name("G").build())
                .genres(
                        List.of(Genre.builder().id(1).name("Комедия").build())
                )
                .build();

        dbFilmStorage.createFilm(existingFilm);

        Film createdFilm = dbFilmStorage.getFilmById(1);

        assertThat(createdFilm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "film")
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 1))
                .hasFieldOrPropertyWithValue("duration", 100)
                .hasFieldOrPropertyWithValue("mpa", MPARating.builder().id(1).name("G").build())
                .hasFieldOrPropertyWithValue("genres", List.of(Genre.builder().id(1).name("Комедия").build()));
    }

    @Test
    public void testGetNonExistentFilmById() {
        assertThatThrownBy(() -> {
            dbFilmStorage.getFilmById(-9999);
            ;
        })
                .isInstanceOf(FilmNotFoundException.class)
                .hasMessageContaining("Фильм c ID=-9999 не найден.");
    }

    @Test
    public void testGetAllFilms() {
        Film existingFilm = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(MPARating.builder().id(1).name("G").build())
                .genres(
                        List.of(Genre.builder().id(1).name("Комедия").build())
                )
                .build();

        Collection<Film> existingFilms = List.of(existingFilm);

        dbFilmStorage.createFilm(existingFilm);

        Collection<Film> films = dbFilmStorage.getAllFilms();

        assertThat(films)
                .isNotEmpty()
                .hasSize(1)
                .doesNotHaveDuplicates()
                .containsAll(existingFilms);
    }
}