package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmValidatorTests {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void creteValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void testEmptyNameShouldReturnViolation() {
        Film film = Film.builder()
                .name("")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        ConstraintViolation<Film> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "name");
        assertEquals(violation.getMessage(), "Название фильма не должно быть пустым.");
    }

    @Test
    public void testNonEmptyNameShouldNotReturnViolation() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNullDescriptionShouldReturnViolation() {
        Film film = Film.builder()
                .name("name")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        ConstraintViolation<Film> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "description");
        assertEquals(violation.getMessage(), "Описание фильма должно быть указано.");
    }

    @Test
    public void testDescriptionMoreThan200CharsShouldReturnViolation() {
        Film film = Film.builder()
                .name("name")
                .description("rmihvqqrkbgzgajusfxsitwmaodwiizmekutncxilkynnwtprliozpvtixhrjaphvqeqwoqaffmqchpedlfoamtongueswtvlbcnbvwskirpmnukgyljiyhofxswrlyavnnvmkwsjtekdcrvtccqwnhkgdtgjmfjuizwmnymiryupsjvkxavhnbnewycrgocyffnomglg")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        ConstraintViolation<Film> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "description");
        assertEquals(violation.getMessage(), "Описание фильма не должно превышать 200 символов.");
    }

    @Test
    public void testDescription200CharsShouldNotReturnViolation() {
        Film film = Film.builder()
                .name("name")
                .description("mihvqqrkbgzgajusfxsitwmaodwiizmekutncxilkynnwtprliozpvtixhrjaphvqeqwoqaffmqchpedlfoamtongueswtvlbcnbvwskirpmnukgyljiyhofxswrlyavnnvmkwsjtekdcrvtccqwnhkgdtgjmfjuizwmnymiryupsjvkxavhnbnewycrgocyffnomglg")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testDescriptionShorterThan200CharsShouldNotReturnViolation() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNullReleaseDateShouldReturnViolation() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        ConstraintViolation<Film> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "releaseDate");
        assertEquals(violation.getMessage(), "Дата релиза фильма должна быть указана.");
    }

    @Test
    public void testPreMovieTimesReleaseDateShouldReturnViolation() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1700, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        ConstraintViolation<Film> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "releaseDate");
        assertEquals(violation.getMessage(), "Дата релиза должна быть не раньше 28 декабря 1895 года.");
    }

    @Test
    public void testTheArrivalOfATrainReleaseDateShouldNotReturnViolation() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testCurrentTimesReleaseDateShouldNotReturnViolation() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNullDurationShouldReturnViolation() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        ConstraintViolation<Film> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "duration");
        assertEquals(violation.getMessage(), "Продолжительность фильма должна быть положительной и больше 0.");
    }

    @Test
    public void testNegativeDurationShouldReturnViolation() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(-200)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        ConstraintViolation<Film> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "duration");
        assertEquals(violation.getMessage(), "Продолжительность фильма должна быть положительной и больше 0.");
    }

    @Test
    public void testPositiveDurationShouldNotReturnViolation() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

}
