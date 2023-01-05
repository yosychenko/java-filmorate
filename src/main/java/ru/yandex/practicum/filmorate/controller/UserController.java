package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {
    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        newUser.setId(++idCounter);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        User existingUser = users.get(newUser.getId());
        if (existingUser != null) {
            users.put(newUser.getId(), newUser);
            return newUser;
        }
        throw new UserNotFoundException(newUser.getId());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
