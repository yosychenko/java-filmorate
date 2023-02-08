package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Film {
    @Id
    private long id;
    @NotBlank(message = "Название фильма не должно быть пустым.")
    private String name;
    @NotNull(message = "Описание фильма должно быть указано.")
    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов.")
    private String description;
    @NotNull(message = "Дата релиза фильма должна быть указана.")
    private LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность фильма должна быть положительной и больше 0.")
    private int duration;

    private List<Genre> genres;
    @NotNull(message = "MPA рейтинг фильма должен быть указан.")
    private MPARating mpa;

    private int rate;
}
