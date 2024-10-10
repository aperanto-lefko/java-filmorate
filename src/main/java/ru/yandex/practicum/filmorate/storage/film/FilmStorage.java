package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public interface FilmStorage {

    Map<Integer, Film> getFilms();

    List<Film> getAllFilms();

    int getNextId();

    boolean isIdNull(Integer id);

    boolean isDateNull(LocalDate date);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void checkDate(Film film);

    Film checkForUpdate(Film film);
}
