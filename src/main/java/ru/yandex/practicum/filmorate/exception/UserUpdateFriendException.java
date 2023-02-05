package ru.yandex.practicum.filmorate.exception;

public class UserUpdateFriendException extends RuntimeException {
    public UserUpdateFriendException(long userId, long friendId) {
        super(String.format("Невозможно пользователю с ID=%s добавить в друзья пользователя с ID=%s. " +
                "Возможно, указанных пользователей не существует.", userId, friendId));
    }
}
