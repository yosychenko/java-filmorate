package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int idCounter = 0;

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
        throw new UserNotFoundException();
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
