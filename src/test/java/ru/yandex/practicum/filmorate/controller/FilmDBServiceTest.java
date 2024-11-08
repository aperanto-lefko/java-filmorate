package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmDBService;

import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate") //вместо того, чтобы импортировать все классы
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDBServiceTest {
    private final FilmDBService filmDBService;
    Film film;

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
        FilmDto filmCreate = filmDBService.createFilm(film);
        assertThat(filmCreate)
                .hasFieldOrPropertyWithValue("mpa.name", "G");
    }
    @Test
    public void testGetFilmById(){
        FilmDto filmCreate = filmDBService.createFilm(film);
        FilmDto filmCreateDB = filmDBService.getFilmById(film.getId());
        assertThat(filmCreateDB)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "name");
        assertThat(filmCreate.getGenres())
                .extracting(Genre::getName)
                .contains("Боевик");
    }

}

