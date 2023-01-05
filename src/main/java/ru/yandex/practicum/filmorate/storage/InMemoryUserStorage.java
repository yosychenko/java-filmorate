package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long idCounter = 0;

    @Override
    public User createUser(User newUser) {
        newUser.setId(++idCounter);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        User existingUser = users.get(newUser.getId());
        if (existingUser != null) {
            users.put(newUser.getId(), newUser);
            return newUser;
        }
        throw new UserNotFoundException(newUser.getId());
    }

    @Override
    public User getUserById(long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
