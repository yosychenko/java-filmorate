package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPARatingStorage;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MPARatingController {

    private final MPARatingStorage mpaRatingStorage;

    @Autowired
    public MPARatingController(MPARatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    @GetMapping()
    public Collection<MPARating> getAllMPARatings() {
        return mpaRatingStorage.getAllMPARatings();
    }

    @GetMapping("/{mpaId}")
    public MPARating getMPARatingById(@PathVariable long mpaId) {
        return mpaRatingStorage.getMPARatingById(mpaId);
    }
}
