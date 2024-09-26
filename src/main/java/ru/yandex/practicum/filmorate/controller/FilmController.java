package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController extends BaseController {
    private final Map<Integer, Film> films = new HashMap<>();
    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        checkDate(film);
        film.setId(getNextId(films));
        films.put(film.getId(), film);
        return film;
    }
    @PutMapping
    public Film update(@Valid @RequestBody Film updateFilm) {
        if (isIdNull(updateFilm.getId())) {
            log.error("Пользователь не ввел id");
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(updateFilm.getId())) {
            checkDate(updateFilm);
            films.put(updateFilm.getId(), updateFilm);
            return updateFilm;
        }
        log.error("Фильм с= " + updateFilm.getId() + " не найден");
        throw new ValidationException("Фильм с id = " + updateFilm.getId() + " не найден");
    }
    public void checkDate(Film film) {
        if ((!isDateNull(film.getReleaseDate()))) {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error("Пользователь ввел дату ранее 28.12.1895");
                throw new ValidationException("Дата релиза не может быть раньше 28.12.1985");
            }
        }
    }
}


/* json для фильма

"id": 1,
"name": "Унесенные ветром",
"description": "Американский художественный фильм 1939 года в жанре исторической военной мелодрамы",
"releaseDate": "1939-15-12",
"duration": 90

"id": 2,
"name": "Зеленая миля",
"description": "Американский фэнтезийный драматический фильм",
"releaseDate": "1999-12-15",
"duration": 120

 */