package ru.yandex.practicum.filmorate.exception;

public class UserDeleteFriendException extends RuntimeException {
    public UserDeleteFriendException(long userId, long friendId) {
        super(String.format("Невозможно у пользователя с ID=%s удалить из друзей пользователя с ID=%s. " +
                "Возможно, указанных пользователей не существует.", userId, friendId));
    }
}