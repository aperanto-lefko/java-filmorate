package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.user.FriendsDBStorage;
import ru.yandex.practicum.filmorate.dal.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFriendException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import java.util.stream.Collectors;

@Slf4j
@Service

public class UserDBService {


    private final UserStorage userDBStorage;
    private final FriendsDBStorage friendsDBStorage;

    public UserDBService(@Qualifier("UserDBStorage") UserStorage userDBStorage, FriendsDBStorage friendsDBStorage) {
        this.userDBStorage = userDBStorage;
        this.friendsDBStorage = friendsDBStorage;
    }


    public UserDto createUser(User user) {
        checkForCreate(user);
        return UserMapper.mapToUserDto(userDBStorage.createUser(user));
    }

    public List<UserDto> getUsersList() {
        return listUserToListDto(userDBStorage.getUsersList());

    }

    public UserDto updateUser(User user) {
        checkForUpdate(user);
        return UserMapper.mapToUserDto(userDBStorage.updateUser(user));
    }

    //текущий метод
    public List<UserDto> addFriend(int idUser, int idFriend) {
        if (idUser == idFriend) {
            log.error("Пользователь указал одинаковые id");
            throw new ValidationException("Вы не можете указывать одинаковый id для двух пользователей");
        }
        checkUserId(idUser);
        checkUserId(idFriend);
        if (isNotOnTheFriendsList(idUser, idFriend)) {
            friendsDBStorage.insertFriend(idUser, idFriend, "Не подтверждено");
        } else {
            log.error("Пользователь повторно добавил в друзья пользователя");
            throw new ValidationException("Пользователь с id " + idFriend +
                    " уже есть в списке друзей id " + idUser);
        }
        if (!isNotOnTheFriendsList(idFriend, idUser)) {
            friendsDBStorage.updateFriendStatus(idFriend, idUser, "Подтверждено");
            friendsDBStorage.updateFriendStatus(idUser, idFriend, "Подтверждено");
        }
        return listUserToListDto(friendsDBStorage.getFriendsById(idUser)); //не проходит один тест на друзей Froend

    }

    public List<UserDto> friendsListById(int id) { //поиск друзей со всеми статусами заявки
        checkUserId(id);
        return listUserToListDto(friendsDBStorage.getFriendsById(id));
    }

    public List<UserDto> unfriending(int idUser, int idFriend) {
        checkUserId(idUser);
        checkUserId(idFriend);
        if (friendsDBStorage.checkFriendsInDB(idUser, idFriend).isPresent()) {
            System.out.println("Запись в базе есть");
        }
        if (friendsDBStorage.checkFriendsInDB(idUser, idFriend).isEmpty()) {
            log.error("Пользователи не являются друзьями");
            throw new NotFriendException("Пользователей нет в списке друзей друг у друга");
        }
        friendsDBStorage.deleteFriend(idUser, idFriend);
        if (friendsDBStorage.checkFriendsInDB(idFriend, idUser).isPresent()) {
            friendsDBStorage.updateFriendStatus(idFriend, idUser, "Не подтверждено");
        }
        return listUserToListDto(friendsDBStorage.getFriendsById(idUser));
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
        checkUserId(user.getId());
        return checkForCreate(user);
    }

    public void checkUserId(int id) {
        if (userDBStorage.getUserById(id).isEmpty()) {
            log.error("Пользователь ввел неверный id");
            throw new ValidationException("Пользователя с таким id нет");
        }
    }

    public List<UserDto> listOfMutualFriends(int idUserOne, int idUserTwo) {
        checkUserId(idUserOne);
        checkUserId(idUserTwo);
        return listUserToListDto(friendsDBStorage.getMutualFriends(idUserOne, idUserTwo));
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

    public List<UserDto> listUserToListDto(List<User> listUser) {
        return listUser
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }
}
