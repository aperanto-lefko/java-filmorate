package ru.yandex.practicum.filmorate.dal.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;


@Component
public interface UserStorage {

    List<User> getUsersList();

    User createUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(int id);

}
