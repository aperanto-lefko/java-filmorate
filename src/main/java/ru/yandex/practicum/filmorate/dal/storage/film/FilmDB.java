package ru.yandex.practicum.filmorate.dal.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Component
public interface FilmDB {
    Film createFilm(Film film, int ratingId);
    Film updateFilm(Film film,int ratingId);
    Optional<Film>findByID(long id);
    List<Film> getAllFilms();
}
