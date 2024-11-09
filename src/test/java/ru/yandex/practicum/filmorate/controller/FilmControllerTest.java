package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    FilmController filmController;


    @Test
    void testCreateFilm() {
        FilmDto film = filmController.create(Film.builder()
                .name("name")
                .description("description")
                .duration(90)
                .releaseDate(LocalDate.parse("1939-12-12"))
                .mpa(Mpa.builder()
                        .id(1)
                        .build())
                .genres(Arrays.asList(Genre.builder()
                        .id(6)
                        .build()))
                .build());
        assertEquals("name", film.getName(), "Метод работает некорректно");
    }

    @Test
    void checkDate() {
        Film film = Film.builder()
                .name("name")
                .releaseDate(LocalDate.of(1794, 12, 5))
                .build();
        Exception e = assertThrows(BadRequestException.class, () -> filmController.create(film),
                "Метод работает некорректно");
        assertTrue(e.getMessage().contains("Дата релиза не может быть раньше 28.12.1985"));
    }
}


