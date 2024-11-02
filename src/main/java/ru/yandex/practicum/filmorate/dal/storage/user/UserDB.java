package ru.yandex.practicum.filmorate.dal.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDB {
    List<User> getAllUsers();
    User createUser (User user);
    Optional<User> findByID(long id);
    User updateUser(User user);
}
