package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final UserService userService;

    @Getter
    private final Map<Integer, List<User>> likes = new HashMap<>(); //лайки

    public Film findFilm(int id) {
        return filmStorage.getFilms().values().stream()
                .filter(film -> film.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
    }

    public String addLike(int idFilm, int idUser) { //добавить проверку на повторны лайк
        Film film = findFilm(idFilm);
        User user = userService.findUser(idUser);
        List<User> list = likes.isEmpty() | !likes.containsKey(idFilm) ? new ArrayList<>() :
                likes.get(idFilm);
        if(list.contains(user)) {
            log.error("Пользователь повторно поставил лайк");
            throw new ValidationException("Пользователь " + user.getName() + " уже голосовал за фильм " + film.getName());
        }
        list.add(user);
        likes.put(idFilm, list);
        film.setLike(likes.get(idFilm).size());
        return "Лайк " + user.getName() + " учтен в пользу фильма " + film.getName();
    }

    public String unlike(int idFilm, int idUser) {
        Film film = findFilm(idFilm);
        User user = userService.findUser(idUser);
        likes.put(idFilm, listWithDeletedFilm(idFilm, idUser));
        film.setLike(likes.get(idFilm).size());
        checkListFilm(idFilm);
        return "Пользователи " + user.getName() +
                " снял лайк фильму " + film.getName();
    }

    public List<User> listWithDeletedFilm(int idFilm, int idUser) {
        return likes.get(idFilm).stream()
                .filter(user -> user.getId() != idUser)
                .toList();
    }

    public void checkListFilm(int id) {
        if (likes.get(id).isEmpty()) {
            likes.remove(id);
        }
    }

    public Map<String, Integer> popularFilm() {
        Map<String, Integer> popularFilms = new LinkedHashMap<>();
        filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparing(Film::getLike).reversed())
                .forEach(film -> popularFilms.put(film.getName(), film.getLike()));
        return popularFilms;
    }
}

