package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.friends.FriendsDBStorage;
import ru.yandex.practicum.filmorate.dal.storage.status.StatusDBStorage;
import ru.yandex.practicum.filmorate.dal.storage.user.UserDB;
import ru.yandex.practicum.filmorate.dal.storage.user.UserDBStorage;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("UserDBStorage")
public class UserDBService {

    private final UserDB userDBStorage;
    private final FriendsDBStorage friendsDBStorage;
    private final StatusDBStorage statusDBStorage;

    public UserDto createUser(User user) {
        checkUserPresence(user);
        checkForCreate(user);
        return UserMapper.mapToUserDto(userDBStorage.createUser(user));
    }

    public List<UserDto> getUsersList() {
        return userDBStorage.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(User user) {
        checkForUpdate(user);
        return UserMapper.mapToUserDto(userDBStorage.updateUser(user));
    }

    //текущий метод
    public List<User> addFriend(int idUser, int idFriend) {
        int statusFriend;
        int statusNotFriend;
        if (statusDBStorage.getStatusId("Подтверждено").isPresent() && statusDBStorage.getStatusId("Неподтверждено").isPresent()) {
            statusFriend = statusDBStorage.getStatusId("Подтверждено").get();
            statusNotFriend = statusDBStorage.getStatusId("Неподтверждено").get();
        } else {
            throw new BadRequestException("Статус не обнаружен");
        }
        if (idUser == idFriend) {
            log.error("Пользователь указал одинаковые id");
            throw new ValidationException("Вы не можете указывать одинаковый id для двух пользователей");
        }
        if (isNotOnTheFriendsList(idUser, idFriend)){
          if (isNotOnTheFriendsList(idFriend, idUser)) {
            friendsDBStorage.insertFriend(idUser, idFriend, statusNotFriend);
        } else friendsDBStorage.updateFriendStatus(idFriend, idUser, statusFriend);
        } else {log.error("Пользователь повторно добавил в друзья пользователя");
            throw new ValidationException("Пользователь с id " + idFriend +
                    " уже есть в списке друзей id " + idUser);

        }
        return friendsDBStorage.getFriends(idUser,statusFriend);
    }

    public boolean isNotOnTheFriendsList(int idUser, int idFriend) {
        return friendsDBStorage.checkFriendsInDB(idUser, idFriend).isEmpty();
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

    public void checkUserPresence(User user) {
        if (userDBStorage.findByEmail(user.getEmail()).isPresent()) {
            log.error("Пользователь с таким имейл уже существует");
            throw new BadRequestException("Пользователь с таким имейл уже существует");
        }
    }

    public boolean isIdNull(int id) {
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
