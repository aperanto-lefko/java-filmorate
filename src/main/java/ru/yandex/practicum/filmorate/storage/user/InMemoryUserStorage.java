package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private int id = 1;
    @Getter
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public int getNextId() {
        return id++;
    }

    @Override
    public boolean isIdNull(Integer id) {
        return id == 0;
    }

    @Override
    public boolean isDateNull(LocalDate date) {
        return date == null;
    }

    @Override
    public boolean isLineBlank(String value) {
        return value.isBlank();
    }

    @Override
    public boolean isValueNull(String value) {
        return value == null;
    }


    @Override
    public User createUser(User user) {
        User chekingUser = checkForCreate(user);
        chekingUser.setId(getNextId());
        users.put(user.getId(), chekingUser);
        return chekingUser;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), checkForUpdate(user));
        return user;
    }

    @Override
    public User checkForCreate(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Пользователь ввел логин с пробелом");
            throw new BadRequestException("Поле логин не может содержать пробелы");
        }
        if (isValueNull(user.getName()) || isLineBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        if (!isDateNull(user.getBirthday())) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Пользователь ввел дату из будущего");
                throw new BadRequestException("Дата рождения не может быть в будущем");
            }
        }
        return user;
    }

    @Override
    public User checkForUpdate(User user) {
        if (isIdNull(user.getId())) {
            log.error("Пользователь не ввел id");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(user.getId())) {
            return checkForCreate(user);
        }
        log.error("Фильм с= " + user.getId() + " не найден");
        throw new ValidationException("Фильм с id = " + user.getId() + " не найден");
    }
}

