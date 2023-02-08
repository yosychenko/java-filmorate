package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
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
class DbUserStorageTest {
    private final DbUserStorage dbUserStorage;

    @Test
    public void testCreateUser() {
        User user = User.builder()
                .email("user1@example.com")
                .login("login1")
                .name("user1")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        User createdUser = dbUserStorage.createUser(user);

        assertThat(createdUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("email", "user1@example.com")
                .hasFieldOrPropertyWithValue("login", "login1")
                .hasFieldOrPropertyWithValue("name", "user1")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 1));
    }

    @Test
    public void testUpdateUser() {
        User existingUser = User.builder()
                .email("user1@example.com")
                .login("login1")
                .name("user1")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        User newUser = User.builder()
                .id(1)
                .email("user_updated@example.com")
                .login("login_updated")
                .name("user_updated")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        dbUserStorage.createUser(existingUser);
        User updatedUser = dbUserStorage.updateUser(newUser);

        assertThat(updatedUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("email", "user_updated@example.com")
                .hasFieldOrPropertyWithValue("login", "login_updated")
                .hasFieldOrPropertyWithValue("name", "user_updated")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 1));
    }

    @Test
    public void testUpdateNonExistentUser() {
        User newUser = User.builder()
                .id(-9999)
                .email("user_updated@example.com")
                .login("login_updated")
                .name("user_updated")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        assertThatThrownBy(() -> {
            dbUserStorage.updateUser(newUser);
        })
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Пользователь c ID=-9999 не найден.");
    }

    @Test
    public void testGetUserById() {
        User user = User.builder()
                .email("user1@example.com")
                .login("login1")
                .name("user1")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        dbUserStorage.createUser(user);
        User createdUser = dbUserStorage.getUserById(1);

        assertThat(createdUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("email", "user1@example.com")
                .hasFieldOrPropertyWithValue("login", "login1")
                .hasFieldOrPropertyWithValue("name", "user1")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 1));
    }

    @Test
    public void testGetNonExistentUserById() {
        assertThatThrownBy(() -> {
            dbUserStorage.getUserById(-9999);
        })
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Пользователь c ID=-9999 не найден.");
    }

    @Test
    public void testGetAllUsers() {
        User user1 = User.builder()
                .email("user1@example.com")
                .login("login1")
                .name("user1")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        User user2 = User.builder()
                .email("user_updated@example.com")
                .login("login_updated")
                .name("user_updated")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        User createdUser1 = dbUserStorage.createUser(user1);
        User createdUser2 = dbUserStorage.createUser(user2);
        Collection<User> allUsers = dbUserStorage.getAllUsers();

        assertThat(allUsers)
                .isNotEmpty()
                .hasSize(2)
                .doesNotHaveDuplicates()
                .containsAll(List.of(createdUser1, createdUser2));
    }
}