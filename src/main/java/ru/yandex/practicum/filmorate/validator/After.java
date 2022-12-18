package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// https://stackoverflow.com/a/53763858
@Constraint(validatedBy = AfterValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface After {
    String message() default "must be after {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();
}