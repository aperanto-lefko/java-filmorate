package ru.yandex.practicum.filmorate.dal.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

@Component
public interface FilmStorage {

    List<Film> getAllFilms();

    List<Film> popularFilm(String size);

    Film createFilm(Film film);

    Film createDBFilm(Film film, int id);

    Film updateFilm(Film film);

}
