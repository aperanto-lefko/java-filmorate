package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public interface UserStorage {


    Map<Integer, User> getUsers();

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
