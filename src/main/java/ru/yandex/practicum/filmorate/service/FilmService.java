package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Getter
    private final Map<Integer, List<User>> likes = new HashMap<>(); //лайки


    public Film findFilm(int id) {
        return filmStorage.getFilms().values().stream()
                .filter(film -> film.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
    }

    public List<User> addLike(int idFilm, int idUser) { //добавить проверку на повторны лайк //возвращать фильм
        Film film = findFilm(idFilm);
        User user = userService.findUser(idUser);
        List<User> list = likes.isEmpty() | !likes.containsKey(idFilm) ? new ArrayList<>() :
                likes.get(idFilm);
        if (list.contains(user)) {
            log.error("Пользователь повторно поставил лайк");
            throw new ValidationException("Пользователь " + user.getName() + " уже голосовал за фильм " + film.getName());
        }
        list.add(user);
        likes.put(idFilm, list);
        film.setLike(likes.get(idFilm).size());
        return likes.get(idFilm);
    }

    public String unlike(int idFilm, int idUser) {
        Film film = findFilm(idFilm);
        User user = userService.findUser(idUser);
        likes.put(idFilm, listWithDeletedFilm(idFilm, idUser));
        film.setLike(likes.get(idFilm).size());
        checkListFilm(idFilm);
        return "Пользователь " + user.getName() +
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

       public List<Film> popularFilm(String size) {
        int count = size == null ? 10 : Integer.parseInt(size);
        if (count > filmStorage.getFilms().size()) {
            count = filmStorage.getFilms().size();
        } else if (count == 0) {
            count = 10;
        }
        List<Film> popularFilms = new LinkedList<>();
        filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparing(Film::getLike).reversed())
                .limit(count)
                .forEach(popularFilms::add);
        return popularFilms;
    }
}

