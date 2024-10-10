package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    final public InMemoryUserStorage inMemoryUserStorage;
    final public UserService userService;

    @GetMapping //список пользователей
    public List<User> findAll() {
        return inMemoryUserStorage.getUsersList();
    }


    @GetMapping("friends") //список дружбы
    public Map<Integer, List<User>> findAllFriends() {
        return userService.getFriendsList();
    }


    @GetMapping("/{id}/friends") //друзья конкретного пользователя
    public List<User> findFriendsIdUser(@PathVariable int id) {
        return userService.friendsListById(id);
    }
    //строка запроса http://localhost:8080/friends/3

    @GetMapping("/{id}/friends/common/{otherId}") // список друзей, общих с другим пользователем.
    public List<User> mutualFriends(@PathVariable Map<String, String> allParam) {
        return userService.listOfMutualFriends(Integer.parseInt(allParam.get("id")), Integer.parseInt(allParam.get("otherId")));
    }
    //строка запроса http://localhost:8080/users/1/friends/common/3

    @PostMapping //создание пользователя
    public User create(@Valid @RequestBody User user) {
        return inMemoryUserStorage.createUser(user);
    }

    @PutMapping //обновление пользователя
    public User update(@Valid @RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}") //подружить пользователей
    public List<User> addFriend(@PathVariable Map<String, String> allParam) {
        return userService.addFriend(Integer.parseInt(allParam.get("id")), Integer.parseInt(allParam.get("friendId")));
    }
    //строка запроса http://localhost:8080/users/1/friends/3

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<User> deleteFriend(@PathVariable Map<String, String> allParam) {
        return userService.unfriending(Integer.parseInt(allParam.get("id")), Integer.parseInt(allParam.get("friendId")));
    }
}

/*
user для json

"id": 1;
"email": "user@mail.ru",
"login": "medved",
"name": "Petr Novikov",
"birthday": "1985-12-05"

"id": 1;
"email": "polk@mail.ru",
"login": "lis",
"birthday": "1954-12-05"

 */