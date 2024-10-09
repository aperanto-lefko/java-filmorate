package ru.yandex.practicum.filmorate.storage.film;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Getter
    private final Map<Integer, Film> films = new HashMap<>();
    private final LocalDate release = LocalDate.of(1895, 12, 28);
    private int id = 1;


    @Override
        public int getNextId () {
        return id++;
    }

        @Override
        public boolean isIdNull (Integer id){
        return id == 0;
    }

        @Override
        public boolean isDateNull (LocalDate date){
        return date == null;
    }

        @Override
        public Film createFilm (Film film){
        checkDate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

        @Override
        public Film updateFilm (Film film){
        films.put(film.getId(), checkForUpdate(film));
        return film; //протестировать фильм без id
    }

        @Override
        public void checkDate (Film film){
        if (!isDateNull(film.getReleaseDate()) && film.getReleaseDate().isBefore(release)) {
            log.error("Пользователь ввел дату ранее 28.12.1895");
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1985");
        }
    }
        @Override
        public Film checkForUpdate (Film film){
        if (isIdNull(film.getId())) {
            log.error("Пользователь не ввел id");
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(film.getId())) {
            checkDate(film);
            return film;
        }
        log.error("Фильм с= " + film.getId() + " не найден");
        throw new ValidationException("Фильм с id = " + film.getId() + " не найден");
    }
       }

