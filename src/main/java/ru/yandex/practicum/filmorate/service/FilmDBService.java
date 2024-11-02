package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmDB;
import ru.yandex.practicum.filmorate.dal.storage.rating.RatingDBStorage;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmDBService {
    @Qualifier("FilmDBStorage") //внедряем экземпляр FilmDB, т.к. интерфейс может реализовывать несколько классов
    private final FilmDB filmDBStorage;
    private final RatingDBStorage ratingDBStorage;
    private static final LocalDate release = LocalDate.of(1895, 12, 28);

    public FilmDto createFilm(Film film) { //добавить добавление в базу рейтингов
        checkForCreate(film);
        return FilmMapper.mapToFilmDto(filmDBStorage.createFilm(film, getRatingID(film))); //добавили фильм+id рейтинга, добавили в базу

    }

    public FilmDto updateFilm(Film film) {
        checkForUpdate(film);
        return FilmMapper.mapToFilmDto(filmDBStorage.updateFilm(film,getRatingID(film)));
    }

    public Film checkForCreate(Film film) {
        checkDate(film);
        return film;
    }

    public void checkDate(Film film) {
        if (!isDateNull(film.getReleaseDate()) && film.getReleaseDate().isBefore(release)) {
            log.error("Пользователь ввел дату ранее 28.12.1895");
            throw new BadRequestException("Дата релиза не может быть раньше 28.12.1985");
        }
    }

    public boolean isDateNull(LocalDate date) {
        return date == null;
    }

    public Film checkForUpdate(Film film) {
        if (isIdNull(film.getId())) {
            log.error("Пользователь не ввел id");
            throw new ValidationException("Id должен быть указан");
        }
        if (filmDBStorage.findByID(film.getId()).isPresent()) {
            checkDate(film);
            return film;
        }
        log.error("Фильм с= " + film.getId() + " не найден");
        throw new ValidationException("Фильм с id = " + film.getId() + " не найден");
    }

    public boolean isIdNull(long id) {
        return id == 0;
    }

    public int getRatingID(Film film) {

        Optional<Integer> ratingId = ratingDBStorage.getRatingId(film.getRating());
        if (ratingId.isEmpty()) {
            throw new NotFoundException("Такого жанра фильма нет");
        }return ratingId.get();
    }
}
