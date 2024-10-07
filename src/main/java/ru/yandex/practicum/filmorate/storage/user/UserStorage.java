package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
public interface UserStorage {
    int getNextId();

    boolean isIdNull(Integer id);

    boolean isDateNull(LocalDate date);

    boolean isLineBlank(String value);

    boolean isValueNull(String value);

    User createUser(User user);

    User updateUser(User user);

    User checkForCreate(User user);

    User checkForUpdate(User user);
}
