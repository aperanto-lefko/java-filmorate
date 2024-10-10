package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    UserController usercontroller;

    @Test
    public void checkLoginField() {

        User validUser = User.builder()
                .name("name")
                .login("login user")
                .build();
        Exception e = assertThrows(BadRequestException.class, () -> usercontroller.create(validUser),
                "Метод работает некорректно");
        assertTrue(e.getMessage().contains("Поле логин не может содержать пробелы"));
    }
}