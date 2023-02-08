package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    @Id
    private long id;
    @NotBlank(message = "Электронная почта не может быть пустой.")
    @Email(message = "Некорректный формат электронной почты.")
    private String email;
    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы.")
    private String login;
    private String name;
    @NotNull(message = "Дата рождения должна быть указана.")
    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;

    public String getName() {
        if (StringUtils.isBlank(name)) {
            return login;
        }
        return name;
    }
}
