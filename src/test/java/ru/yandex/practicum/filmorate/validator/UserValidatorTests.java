package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserValidatorTests {

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
    public void testEmptyEmailShouldReturnViolation() {
        User user = User.builder()
                .email("")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "email");
        assertEquals(violation.getMessage(), "Электронная почта не может быть пустой.");

    }

    @Test
    public void testNonValidEmailShouldReturnViolation() {
        User user = User.builder()
                .email("gmail")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "email");
        assertEquals(violation.getMessage(), "Некорректный формат электронной почты.");

    }

    @Test
    public void testValidEmailShouldNotReturnViolation() {
        User user = User.builder()
                .email("example@gmail.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testEmptyLoginShouldReturnViolation() {
        User user = User.builder()
                .email("example@gmail.com")
                .login("")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "login");
        assertEquals(violation.getMessage(), "Логин не может быть пустым.");
    }

    @Test
    public void testLoginContainsSpacesShouldReturnViolation() {
        User user = User.builder()
                .email("example@gmail.com")
                .login("login with spaces")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "login");
        assertEquals(violation.getMessage(), "Логин не может содержать пробелы.");
    }

    @Test
    public void testValidLoginShouldNotReturnViolation() {
        User user = User.builder()
                .email("example@gmail.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNullDOBShouldReturnViolation() {
        User user = User.builder()
                .email("example@gmail.com")
                .login("login")
                .name("name")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "birthday");
        assertEquals(violation.getMessage(), "Дата рождения должна быть указана.");
    }

    @Test
    public void testDOBSInTheFutureShouldReturnViolation() {
        User user = User.builder()
                .email("example@gmail.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2049, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(violation.getPropertyPath().toString(), "birthday");
        assertEquals(violation.getMessage(), "Дата рождения не может быть в будущем.");
    }

    @Test
    public void testDOBSTodayShouldNotReturnViolation() {
        User user = User.builder()
                .email("example@gmail.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testDOBSInThePastShouldNotReturnViolation() {
        User user = User.builder()
                .email("example@gmail.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1999, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }
}
