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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor //Autowired добавится автоматически
public class FilmController {

    final FilmService filmService;
    final InMemoryFilmStorage inMemoryFilmStorage;

    @GetMapping
    public List<Film> findAll() {
        return inMemoryFilmStorage.getAllFilms();
    }

    @GetMapping("/likes") //список лайков
    public Map<Integer, List<User>> findAllLikes() {
        return filmService.getLikes();
    }

    @GetMapping("/popular") //список популярных фильмов
    public List<Film> findPopularFilm(@RequestParam(defaultValue = "10") String count) {
        return inMemoryFilmStorage.popularFilm(count);
    }

    //строка запроса http://localhost:8080/films/popular?count=4
    @PutMapping("/{id}/like/{userId}") //поставить лайк
    public List<User> addLike(@PathVariable Map<String, String> allParam) {
        return filmService.addLike(Integer.parseInt(allParam.get("id")), Integer.parseInt(allParam.get("userId")));
    }

    //строка запроса http://localhost:8080/films/1/like/3
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.createFilm(filmService.checkForCreate(film));
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.updateFilm(filmService.checkForUpdate(film));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String deleteLike(@PathVariable Map<String, String> allParam) {
        return filmService.unlike(Integer.parseInt(allParam.get("id")), Integer.parseInt(allParam.get("userId")));
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