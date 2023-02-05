package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserStorage userStorage;

    @Autowired
    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        return userStorage.createUser(newUser);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        return userStorage.updateUser(newUser);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userStorage.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        userStorage.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable long id) {
        return userStorage.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

}
