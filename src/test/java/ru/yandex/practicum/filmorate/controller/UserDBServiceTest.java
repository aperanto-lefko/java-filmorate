package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDBService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate") //вместо того, чтобы импортировать все классы
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDBServiceTest {
    private final UserDBService userDBService;
    User u1, u2, failUser;

    @BeforeEach
    public void createUsers() {
        u1 = User.builder()
                .name("Name U1")
                .email("mailU1@mail.ru")
                .login("loginU1")
                .birthday(LocalDate.parse("1985-12-05"))
                .build();
        u2 = User.builder()
                .name("Name U2")
                .email("mailU2@mail.ru")
                .login("loginU2")
                .birthday(LocalDate.parse("1990-10-01"))
                .build();
        failUser = User.builder()
                .name("Name failUser")
                .email("mailfU@mail.ru")
                .login("login fU")
                .birthday(LocalDate.parse("1944-05-20"))
                .build();
    }

    @Test
    public void testCreateUser() {
        UserDto u1Dto = userDBService.createUser(u1);
        assertThat(u1Dto)
                .hasFieldOrPropertyWithValue("name", "Name U1")
                .hasFieldOrPropertyWithValue("email", "mailU1@mail.ru");
    }

    @Test
    public void testCreateFailUser() {
        Exception e = assertThrows(BadRequestException.class, () -> userDBService.createUser(failUser),
                "Метод работает некорректно");
        assertTrue(e.getMessage().contains("Поле логин не может содержать пробелы"));
    }

    @Test
    public void testAddFriend() {
        UserDto u1Dto = userDBService.createUser(u1);
        UserDto u2Dto = userDBService.createUser(u2);
        List<UserDto> list = userDBService.addFriend(u1Dto.getId(), u2Dto.getId());
        assertThat(list)
                .hasSize(1)
                .extracting(UserDto::getId) //список id юзеров
                .contains(u2Dto.getId()); ////проверка что список содержит 1 польз., и его id соотв.второму
        Exception e = assertThrows(ValidationException.class, () -> userDBService.addFriend(u1Dto.getId(), u2Dto.getId()),
                "Метод работает некорректно");
        assertTrue(e.getMessage().contains("Пользователь с id " + u2Dto.getId() + " уже есть в списке друзей id " + u1Dto.getId()));
    }
}
