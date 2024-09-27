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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<User>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        User chekingUser = checkForCreate(user);
        chekingUser.setId(getNextId());
        users.put(user.getId(), chekingUser);
        return chekingUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User updateUser) {
        users.put(updateUser.getId(), checkForUpdate(updateUser));
        return checkForUpdate(updateUser);
    }

    public User checkForCreate(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Пользователь ввел логин с пробелом");
            throw new ValidationException("Поле логин не может содержать пробелы");
        }
        if (isValueNull(user.getName()) || isLineBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        if (!isDateNull(user.getBirthday())) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Пользователь ввел дату из будущего");
                throw new ValidationException("Дата рождения не может быть в будущем");
            }
        }
        return user;
    }

    public User checkForUpdate(User updateUser) {
        if (isIdNull(updateUser.getId())) {
            log.error("Пользователь не ввел id");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(updateUser.getId())) {
            User chekingUser = checkForCreate(updateUser);
            return chekingUser;
        }
        log.error("Фильм с= " + updateUser.getId() + " не найден");
        throw new ValidationException("Фильм с id = " + updateUser.getId() + " не найден");
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