package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.dal.storage.rating.RatingDBStorage;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmDBServise {
    @Qualifier("FilmDBStorage")
    private final FilmStorage filmDBStorage;
    private final RatingDBStorage ratingDBStorage;
    private static final LocalDate release = LocalDate.of(1895, 12, 28);

    public FilmDto createFilm(Film film) {
        //получить номер id для id рейтинга
        Optional<Integer> rating_id= ratingDBStorage.getRatingId(film.getRating());
        if (rating_id.isEmpty()) {
            throw new NotFoundException("Такого жанра фильма нет");
        }
        filmDBStorage.createDBFilm(film, rating_id.get()); //добавили фильм+id рейтинга
    }
}
