package ru.yandex.practicum.filmorate.dal.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;


public interface UserStorage {

    List<User> getUsersList();

    User createUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(int id);

}
