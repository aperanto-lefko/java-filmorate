package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @Test
    void checkDate() {
        Film validFilm = new Film();
        validFilm.setReleaseDate(LocalDate.of(1794, 12, 5));
        Exception e = assertThrows(ValidationException.class, () -> inMemoryFilmStorage.checkDate(validFilm),
                "Метод работает некорректно");
        assertTrue(e.getMessage().contains("Дата релиза не может быть раньше 28.12.1985"));
    }
}
