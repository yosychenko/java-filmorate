package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public abstract class AbstractController<T> {
    protected final Map<Integer, T> users = new HashMap<>();
    protected final Map<Integer, T> films = new HashMap<>();
    protected int idCounter = 0;
}
