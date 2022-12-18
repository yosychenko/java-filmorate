package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

// https://stackoverflow.com/a/53763858
public class AfterValidator implements ConstraintValidator<After, LocalDate> {

    private LocalDate date;

    @Override
    public void initialize(After annotation) {
        date = LocalDate.parse(annotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value != null) {
            if (value.isBefore(date)) {
                valid = false;
            }
        }
        return valid;
    }
}