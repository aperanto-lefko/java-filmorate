package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DataFutureValidator implements ConstraintValidator<DataFutureValid, LocalDate> {

    @Override
    public void initialize(DataFutureValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return !date.isAfter(LocalDate.now());
    }
}
