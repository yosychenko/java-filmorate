package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Component("InMemoryUserStorage")
public class InMemoryUserStorage extends AbstractStorage<User> implements UserStorage {

    @Override
    public User createUser(User newUser) {
        newUser.setId(++idCounter);
        return super.createRecord(newUser.getId(), newUser);
    }

    @Override
    public User updateUser(User newUser) {
        return super.updateRecord(newUser.getId(), newUser, new UserNotFoundException(newUser.getId()));
    }

    @Override
    public User getUserById(long id) {
        return super.getRecordById(id, new UserNotFoundException(id));
    }

    @Override
    public List<User> getAllUsers() {
        return super.getAllRecords();
    }
}
