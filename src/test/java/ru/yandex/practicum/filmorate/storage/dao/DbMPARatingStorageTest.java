package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.MPARatingNotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DbMPARatingStorageTest {

    private final DbMPARatingStorage dbMPARatingStorage;

    @Test
    public void testGetAllMPARatings() {
        Collection<MPARating> mpaRatings = List.of(
                MPARating.builder().id(1).name("G").build(),
                MPARating.builder().id(2).name("PG").build(),
                MPARating.builder().id(3).name("PG-13").build(),
                MPARating.builder().id(4).name("R").build(),
                MPARating.builder().id(5).name("NC-17").build()
        );

        Collection<MPARating> mpaRatingsFromDb = dbMPARatingStorage.getAllMPARatings();

        assertThat(mpaRatingsFromDb)
                .isNotEmpty()
                .hasSize(5)
                .doesNotHaveDuplicates()
                .containsAll(mpaRatings);
    }

    @Test
    public void testGetExistingMPARatingById() {
        MPARating mpaRating = dbMPARatingStorage.getMPARatingById(1);

        assertThat(mpaRating)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void testGetNonExistingMPARatingById() {
        assertThatThrownBy(() -> {
            dbMPARatingStorage.getMPARatingById(-9999);
        })
                .isInstanceOf(MPARatingNotFoundException.class)
                .hasMessageContaining("MPA рейтинг c ID=-9999 не найден.")
        ;
    }

}