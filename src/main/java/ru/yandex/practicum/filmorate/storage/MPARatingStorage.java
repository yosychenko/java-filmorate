package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Collection;

public interface MPARatingStorage {
    Collection<MPARating> getAllMPARatings();

    MPARating getMPARatingById(long id);
}
