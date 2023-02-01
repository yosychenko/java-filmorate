package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User createUser(User newUser);

    User updateUser(User newUser);

    User getUserById(long id);

    Collection<User> getAllUsers();
}
