package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendsStorage {
    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    Collection<User> getFriends(long id);

    Collection<User> getCommonFriends(long id, long otherId);
}
