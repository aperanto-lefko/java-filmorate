package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @Test
    public void checkLoginField() {
        User validUser = new User();
        validUser.setLogin("name user");
        Exception e = assertThrows(ValidationException.class, () -> inMemoryUserStorage.checkForCreate(validUser),
                "Метод работает некорректно");
        assertTrue(e.getMessage().contains("Поле логин не может содержать пробелы"));
    }
}