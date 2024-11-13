package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmDbStorage.class, FilmRowMapper.class})

public class FilmDBStorageTest {
    private final FilmStorage filmDbStorage;

    Film film;

    @Autowired
    public FilmDBStorageTest(@Qualifier("FilmDBStorage") FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;

    }

    @BeforeEach
    public void createTestFilm() {
        film = Film.builder()
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
                .build();
    }

    @Test
    public void testCreateFilm() {

        Film filmCreate = filmDbStorage.createFilm(film);
        assertThat(filmCreate).hasFieldOrPropertyWithValue("id", filmCreate.getId());
    }

    @Test
    public void testSearchFilmById() {
        Film filmCreate = filmDbStorage.createFilm(film);
        Optional<Film> searchFilm = filmDbStorage.findFilmByID(filmCreate.getId());
        assertThat(searchFilm)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", filmCreate.getId()));
    }
}
