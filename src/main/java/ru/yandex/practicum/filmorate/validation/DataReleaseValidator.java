package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DataReleaseValidator implements ConstraintValidator<DataReleaseValid, LocalDate> {
    private static final LocalDate RELEASE = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(DataReleaseValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date.isBefore(RELEASE)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Дата релиза не может быть раньше 28.12.1985")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
