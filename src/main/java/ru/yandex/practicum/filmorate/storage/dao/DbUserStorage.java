package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Component("DbUserStorage")
public class DbUserStorage implements UserStorage {
    @Override
    public User createUser(User newUser) {
        return null;
    }

    @Override
    public User updateUser(User newUser) {
        return null;
    }

    @Override
    public User getUserById(long id) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }
}
