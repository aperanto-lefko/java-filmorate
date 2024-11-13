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
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.service.UserDBService;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    final UserDBService userDBService;

    @PostMapping //создание пользователя
    public UserDto create(@Valid @RequestBody User user) {
        return userDBService.createUser(user);
    }

    @GetMapping //список пользователей
    public List<UserDto> findAll() {
        return userDBService.getUsersList();
    }

    @PutMapping //обновление пользователя
    public UserDto update(@Valid @RequestBody User user) {
        return userDBService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}") //подружить пользователей
    public List<UserDto> addFriend(@PathVariable Map<String, String> allParam) {
        return userDBService.addFriend(Integer.parseInt(allParam.get("id")), Integer.parseInt(allParam.get("friendId")));
    }

    @GetMapping("/{id}/friends") //друзья конкретного пользователя
    public List<UserDto> findFriendsIdUser(@PathVariable int id) {
        return userDBService.friendsListById(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<UserDto> deleteFriend(@PathVariable Map<String, String> allParam) {
        return userDBService.unfriending(Integer.parseInt(allParam.get("id")), Integer.parseInt(allParam.get("friendId")));
    }

    @GetMapping("/{id}/friends/common/{otherId}") // список друзей, общих с другим пользователем.
    public List<UserDto> mutualFriends(@PathVariable Map<String, String> allParam) {
        return userDBService.listOfMutualFriends(Integer.parseInt(allParam.get("id")), Integer.parseInt(allParam.get("otherId")));
    }
}

/*
//строка запроса http://localhost:8080/users/1/friends/3
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