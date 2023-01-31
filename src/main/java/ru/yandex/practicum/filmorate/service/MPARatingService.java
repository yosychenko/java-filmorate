package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPARatingStorage;

import java.util.List;

@Service
public class MPARatingService {
    private final MPARatingStorage mpaRatingStorage;

    @Autowired
    public MPARatingService(MPARatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public List<MPARating> getAllMPARatings() {
        return mpaRatingStorage.getAllMPARatings();
    }

    public MPARating getMPARatingById(long id) {
        return mpaRatingStorage.getMPARatingById(id);
    }
}
