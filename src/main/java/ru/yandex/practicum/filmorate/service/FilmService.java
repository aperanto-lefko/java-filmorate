package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage filmStorage;
    private final UserService userService;
    private static final LocalDate release = LocalDate.of(1895, 12, 28);

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

    public Film checkForUpdate(Film film) {
        if (isIdNull(film.getId())) {
            log.error("Пользователь не ввел id");
            throw new ValidationException("Id должен быть указан");
        }
        if (filmStorage.getFilms().containsKey(film.getId())) {
            checkDate(film);
            return film;
        }
        log.error("Фильм с= " + film.getId() + " не найден");
        throw new ValidationException("Фильм с id = " + film.getId() + " не найден");
    }

    public Film checkForCreate(Film film) {
        checkDate(film);
        return film;
    }

    public void checkDate(Film film) {
        if (!isDateNull(film.getReleaseDate()) && film.getReleaseDate().isBefore(release)) {
            log.error("Пользователь ввел дату ранее 28.12.1895");
            throw new BadRequestException("Дата релиза не может быть раньше 28.12.1985");
        }
    }

    public boolean isIdNull(Integer id) {
        return id == 0;
    }

    public boolean isDateNull(LocalDate date) {
        return date == null;
    }
}

