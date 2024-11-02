package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.user.UserDB;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDBService {
    @Qualifier("UserDBStorage")
    private final UserDB userDBStorage;

    public UserDto createUser(User user){
        checkForCreate(user);
        return UserMapper.mapToUserDto(userDBStorage.createUser(user));
    }
    public List<UserDto>getUsersList() {
    return userDBStorage.getAllUsers()
            .stream()
            .map(UserMapper::mapToUserDto)
            .collect(Collectors.toList());
    }

    public UserDto updateUser(User user) {
        checkForUpdate(user);
        return UserMapper.mapToUserDto(userDBStorage.updateUser(user));
    }

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

    public User checkForUpdate(User user) {
        if (isIdNull(user.getId())) {
            log.error("Пользователь не ввел id");
            throw new ValidationException("Id должен быть указан");
        }
        if (userDBStorage.findByID(user.getId()).isPresent()) {
            return checkForCreate(user);
        }
        log.error("Фильм с= " + user.getId() + " не найден");
        throw new ValidationException("Фильм с id = " + user.getId() + " не найден");
    }

    public boolean isIdNull(Long id) {
        return id == 0;
    }

    public boolean isDateNull(LocalDate date) {
        return date == null;
    }

    public boolean isLineBlank(String value) {
        return value.isBlank();
    }

    public boolean isValueNull(String value) {
        return value == null;
    }
}
