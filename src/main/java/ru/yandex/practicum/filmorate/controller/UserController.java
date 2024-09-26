package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        // проверяем выполнение необходимых условий
        checkLoginField(user);
        replacingNameWithLogin(user);
        checkingDate(user);
        user.setId(getNextId(users));
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User updateUser) {
        if (isIdNull(updateUser.getId())) {
            log.error("Пользователь не ввел id");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(updateUser.getId())) {
            checkLoginField(updateUser);
            replacingNameWithLogin(updateUser);
            checkingDate(updateUser);
            return updateUser;
        }
        log.error("Фильм с= " + updateUser.getId() + " не найден");
        throw new ValidationException("Фильм с id = " + updateUser.getId() + " не найден");
    }

    public void checkLoginField(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Пользователь ввел логин с пробелом");
            throw new ValidationException("Поле логин не может содержать пробелы");
        }
    }

    private void replacingNameWithLogin(User user) {
        if (isValueNull(user.getName()) || isLineBlank(user.getName())) {
            user.setName(user.getLogin());
        }
    }

    private void checkingDate(User user) {
        if (!isDateNull(user.getBirthday())) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Пользователь ввел дату из будущего");
                throw new ValidationException("Дата рождения не может быть в будущем");
            }
        }
    }
}
/*
user для json

"id": 1;
"email": "user@mail.ru",
"login": "medved",
"name": "Petr Novikov",
"birthday": "1985-12-05""

"id": 1;
"email": "polk@mail.ru",
"login": "lis",
"birthday": "1954-12-05"

 */