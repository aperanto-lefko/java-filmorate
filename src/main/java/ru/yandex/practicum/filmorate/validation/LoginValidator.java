package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class LoginValidator implements ConstraintValidator<LoginValid, String> {

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        return !login.contains(" ");
    }
}
