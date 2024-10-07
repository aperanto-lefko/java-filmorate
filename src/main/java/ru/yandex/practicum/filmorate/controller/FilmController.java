package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Map;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor //Autowired добавится автоматически
public class FilmController {
    final public InMemoryFilmStorage inMemoryFilmStorage;

    @GetMapping
    public Map<Integer, Film> findAll() {
        return inMemoryFilmStorage.getFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }
}







/* json для фильма
{
"id": 1,
"name": "Унесенные ветром",
"description": "Американский художественный фильм 1939 года в жанре исторической военной мелодрамы",
"releaseDate": "1939-12-12",
"duration": 90
}
{
"id": 2,
"name": "Зеленая миля",
"description": "Американский фэнтезийный драматический фильм",
"releaseDate": "1999-12-15",
"duration": 120
}
 */