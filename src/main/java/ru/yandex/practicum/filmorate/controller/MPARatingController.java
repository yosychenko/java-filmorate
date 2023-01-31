package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.service.MPARatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MPARatingController {

    private final MPARatingService mpaRatingService;

    @Autowired
    public MPARatingController(MPARatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping()
    public List<MPARating> getAllMPARatings() {
        return mpaRatingService.getAllMPARatings();
    }

    @GetMapping("/{mpaId}")
    public MPARating getMPARatingById(@PathVariable long mpaId) {
        return mpaRatingService.getMPARatingById(mpaId);
    }
}
