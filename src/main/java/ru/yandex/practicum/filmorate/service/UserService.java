package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    @Getter
    private final Map<Integer, List<User>> friendsList = new HashMap<>();

    public List<User> friendsListById(int id) {
        if (!friendsList.containsKey(id)) {
            friendsList.put(findUser(id).getId(), new ArrayList<>());
        }
        return friendsList.get(findUser(id).getId());
    }

    public User findUser(int idUser) {
        return userStorage.getUsers().values().stream()
                .filter(us -> us.getId() == idUser)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + idUser + " не найден"));
    }

    public boolean checkingFriendsList(int idUser, int idFriend) {
        if (friendsList.isEmpty() | !friendsList.containsKey(idUser)) {
            return true;
        }
        return friendsList.get(idUser).stream()
                .map(User::getId)
                .noneMatch(id -> id == idFriend);
    }

    public List<User> addFriend(int idUser, int idFriend) { //добавление в друзья
        User user = findUser(idUser);
        User friend = findUser(idFriend);
        if (idUser == idFriend) {
            log.error("Пользователь указал одинаковые id");
            throw new ValidationException("Вы не можете указывать одинаковый id для двух пользователей");
        }
        if (!checkingFriendsList(idUser, idFriend)) {
            log.error("Пользователь повторно добавил в друзья пользователя");
            throw new ValidationException("Пользователь " + friend.getName() +
                    " уже есть в списке друзей " + user.getName());
        }
        putFriendInList(idUser, idFriend);
        putFriendInList(idFriend, idUser);
        return friendsList.get(idUser);
    }

    public void putFriendInList(int idUser, int idFriend) {
        List<User> list = friendsList.isEmpty() | !friendsList.containsKey(idUser) ? new ArrayList<>() : friendsList.get(idUser);
        list.add(findUser(idFriend));
        friendsList.put(idUser, list);
    }

    public List<User> listOfMutualFriends(int idUserOne, int idUserTwo) { //вывод общего списка друзей
        return friendsList.get(idUserOne).stream()
                .filter(user -> friendsList.get(idUserTwo).contains(user))
                .toList();
    }

    public List<User> unfriending(int idOne, int idTwo) {
        findUser(idOne);
        findUser(idTwo);
        friendsList.put(idOne, listWithDeletedUser(idOne, idTwo));
        friendsList.put(idTwo, listWithDeletedUser(idTwo, idOne));
        return friendsList.get(idOne);
    }

    public List<User> listWithDeletedUser(int idOne, int idTwo) {
        if (!friendsList.containsKey(idOne) | !friendsList.containsKey(idTwo)) {
            return new ArrayList<>();
        }
        return friendsList.get(idOne).stream()
                .filter(user -> user.getId() != idTwo)
                .toList();
    }
}

