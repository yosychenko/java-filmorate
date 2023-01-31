package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("DbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long id, long friendId) {
        User currentUser = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);

        currentUser.getFriends().add(friend.getId());
        friend.getFriends().add(currentUser.getId());
    }

    public void deleteFriend(long id, long friendId) {
        User currentUser = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);

        currentUser.getFriends().remove(friend.getId());
        friend.getFriends().remove(currentUser.getId());
    }

    public List<User> getFriends(long id) {
        User currentUser = userStorage.getUserById(id);

        return currentUser.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {
        User currentUser = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);

        Set<Long> commonFriends = new HashSet<>(currentUser.getFriends());
        commonFriends.retainAll(otherUser.getFriends());

        return commonFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

}
