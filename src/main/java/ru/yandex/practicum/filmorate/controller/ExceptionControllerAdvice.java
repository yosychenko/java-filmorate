package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
class ExceptionControllerAdvice {
    /**
     * Хендлер для исключения MethodArgumentNotValidException.
     * Позволяет выводить в ответ название поля и причину, почему это поле не прошло валидацию.
     *
     * @param ex экземпляр исключения MethodArgumentNotValidException.
     * @return хэш, где ключом является имя поля, а значением - причина, из-за которой это поле не прошло валидацию
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBindValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(ValidationException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler({
            FilmNotFoundException.class,
            UserNotFoundException.class,
            GenreNotFoundException.class,
            MPARatingNotFoundException.class,
            UserFilmAddLikeException.class,
            UserUpdateFriendException.class,
            UserFilmDeleteLikeException.class,
            UserDeleteFriendException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundExceptions(RuntimeException ex) {
        return Map.of("message", ex.getMessage());
    }
}