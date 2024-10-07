package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Component
public interface FilmStorage {
    int getNextId();

    boolean isIdNull(Integer id);

    boolean isDateNull(LocalDate date);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void checkDate(Film film);

    Film checkForUpdate(Film film);
}
