package ru.yandex.practicum.filmorate.dal.storage.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private int id = 1;

    @Getter
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getUsersList() {
        return users.values().stream().toList();
    }


    public int getNextId() {
        return id++;
    }

    @Override
    public User createUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }
    }

