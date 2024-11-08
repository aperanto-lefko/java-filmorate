package ru.yandex.practicum.filmorate.dal.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;


public interface FilmStorage {

    List<Film> getAllFilms();

    List<Film> popularFilm(String size);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> findFilmByID(int id);

}
