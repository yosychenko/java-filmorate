package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class MPARating {
    @Id
    private long id;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rating;
}
