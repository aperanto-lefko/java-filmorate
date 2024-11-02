package ru.yandex.practicum.filmorate.dal.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

@Component
public interface UserStorage {

    //Map<Integer, User> getUsers();

    List<User> getUsersList();

    int getNextId();

    User createUser(User user);

    User updateUser(User user);

}
