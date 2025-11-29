package ru.yandex.practicum.filmorate.validator.release;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;


public class ReleaseDateValidator implements ConstraintValidator<ValidReleaseDate, LocalDate> {

    /**
     * Самая ранняя разрешенная дата релиза фильма.
     */
    private static final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {

        if (localDate == null) {
            return true;
        }

        return !localDate.isBefore(EARLIEST_DATE);
    }
}
