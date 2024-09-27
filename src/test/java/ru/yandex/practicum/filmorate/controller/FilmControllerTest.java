package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    static FilmController filmController = new FilmController();

    @Test
    void checkDate() {
        Film validFilm = new Film();
        validFilm.setReleaseDate(LocalDate.of(1794, 12, 5));
        Exception e = assertThrows(ValidationException.class, () -> filmController.checkDate(validFilm),
                "Метод работает некорректно");
        assertTrue(e.getMessage().contains("Дата релиза не может быть раньше 28.12.1985"));
    }
}
