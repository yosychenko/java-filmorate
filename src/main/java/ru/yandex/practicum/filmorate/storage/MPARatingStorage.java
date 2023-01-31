package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.List;

public interface MPARatingStorage {
    List<MPARating> getAllMPARatings();

    MPARating getMPARatingById(long id);
}
