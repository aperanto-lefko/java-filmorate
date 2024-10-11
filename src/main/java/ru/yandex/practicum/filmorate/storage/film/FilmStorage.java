package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

@Component
public interface FilmStorage {

    Map<Integer, Film> getFilms();

    List<Film> getAllFilms();

    List<Film> popularFilm(String size);

    int getNextId();

    Film createFilm(Film film);

    Film updateFilm(Film film);

}
