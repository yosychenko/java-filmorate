package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DbGenreStorageTest {
    private final DbGenreStorage dbGenreStorage;

    @Test
    public void testGetAllGenres() {
        Collection<Genre> genres = List.of(
                Genre.builder().id(1).name("Комедия").build(),
                Genre.builder().id(2).name("Драма").build(),
                Genre.builder().id(3).name("Мультфильм").build(),
                Genre.builder().id(4).name("Триллер").build(),
                Genre.builder().id(5).name("Документальный").build(),
                Genre.builder().id(6).name("Боевик").build()
        );

        Collection<Genre> genresFromDB = dbGenreStorage.getAllGenres();

        assertThat(genresFromDB)
                .isNotEmpty()
                .hasSize(6)
                .doesNotHaveDuplicates()
                .containsAll(genres);
    }

    @Test
    public void testGetExistingGenreById() {
        Genre genre = dbGenreStorage.getGenreById(1);

        assertThat(genre)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void testGetNonExistingGenreById() {
        assertThatThrownBy(() -> {
            dbGenreStorage.getGenreById(-9999);
        })
                .isInstanceOf(GenreNotFoundException.class)
                .hasMessageContaining("Жанр c ID=-9999 не найден.")
        ;
    }
}