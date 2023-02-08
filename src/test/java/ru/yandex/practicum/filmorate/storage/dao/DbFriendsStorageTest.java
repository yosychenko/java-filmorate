package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.UserDeleteFriendException;
import ru.yandex.practicum.filmorate.exception.UserUpdateFriendException;
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
class DbFriendsStorageTest {

    private final DbFriendsStorage dbFriendsStorage;
    private final DbUserStorage dbUserStorage;

    @Test
    public void testAddFriendAndGetFriends() {
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

        dbFriendsStorage.addFriend(createdUser1.getId(), createdUser2.getId());
        Collection<User> friends = dbFriendsStorage.getFriends(createdUser1.getId());

        assertThat(friends)
                .isNotEmpty()
                .hasSize(1)
                .doesNotHaveDuplicates()
                .containsAll(List.of(createdUser2));

    }

    @Test
    public void testAddNonExistentUserAsFriend() {
        assertThatThrownBy(() -> {
            dbFriendsStorage.addFriend(-9999, -9999);
        })
                .isInstanceOf(UserUpdateFriendException.class)
                .hasMessageContaining("Невозможно пользователю с ID=-9999 добавить в друзья пользователя с ID=-9999. " +
                        "Возможно, указанных пользователей не существует.");
    }

    @Test
    public void testDeleteFriendAndGetFriends() {
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

        dbFriendsStorage.addFriend(createdUser1.getId(), createdUser2.getId());
        dbFriendsStorage.deleteFriend(createdUser1.getId(), createdUser2.getId());
        Collection<User> friends = dbFriendsStorage.getFriends(createdUser1.getId());

        assertThat(friends)
                .isEmpty();
    }

    @Test
    public void testDeleteNonExistentUserAsFriend() {
        assertThatThrownBy(() -> {
            dbFriendsStorage.deleteFriend(-9999, -9999);
        })
                .isInstanceOf(UserDeleteFriendException.class)
                .hasMessageContaining("Невозможно у пользователя с ID=-9999 удалить из друзей пользователя с ID=-9999. " +
                        "Возможно, указанных пользователей не существует.");
    }

    @Test
    public void testGetCommonFriends() {
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
        User user3 = User.builder()
                .email("user_updated3@example.com")
                .login("login_updated3")
                .name("user_updated3")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        User createdUser1 = dbUserStorage.createUser(user1);
        User createdUser2 = dbUserStorage.createUser(user2);
        User createdUser3 = dbUserStorage.createUser(user3);

        // Пользователи 1 и 2 отправили заявку в друзья пользователю 3
        dbFriendsStorage.addFriend(createdUser1.getId(), createdUser3.getId());
        dbFriendsStorage.addFriend(createdUser2.getId(), createdUser3.getId());

        // Общий друг - пользователь 3
        Collection<User> commonFriends = dbFriendsStorage.getCommonFriends(1, 2);

        assertThat(commonFriends)
                .isNotEmpty()
                .hasSize(1)
                .doesNotHaveDuplicates()
                .containsAll(List.of(createdUser3));
    }

    @Test
    public void testGetCommonFriendsNoCommonFriends() {
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
        User user3 = User.builder()
                .email("user_updated3@example.com")
                .login("login_updated3")
                .name("user_updated3")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        User createdUser1 = dbUserStorage.createUser(user1);
        User createdUser2 = dbUserStorage.createUser(user2);
        User createdUser3 = dbUserStorage.createUser(user3);

        // Пользователи 1 и 2 отправили заявку в друзья пользователю 3
        dbFriendsStorage.addFriend(createdUser1.getId(), createdUser3.getId());
        dbFriendsStorage.addFriend(createdUser2.getId(), createdUser3.getId());

        // Пользователь 3 заявку в друзья никому не отправлял, поэтому у него нет общих друзей с пользователем 1
        Collection<User> commonFriends = dbFriendsStorage.getCommonFriends(1, 3);

        assertThat(commonFriends)
                .isEmpty();
    }
}