package ru.yandex.practicum.filmorate.dal.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.DBStorage;
import ru.yandex.practicum.filmorate.dal.storage.rating.RatingDBStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository("FilmDBStorage")
//аннотация для класса, чтобы добавлялись в контекст, перехват исключений, которые потом преобразовывает в стандартные исключения Spring — DataAccessException
public class FilmDbStorage extends DBStorage implements FilmDB { //слой DAO логика работы с таблицей film


    private static final String FIND_All_QUERY = "SELECT * FROM films";

    private static final String INSERT_QUERY = "INSERT INTO films (name, description, releaseDate, duration, rating_id) VALUES (?, ?, ?, ?, ?) returning id";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";

    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ?, WHERE id = ?";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper,Film.class);
    }

    public List<Film> getAllFilms() {
        return findMany(FIND_All_QUERY);
    }

    //public List<Film> popularFilm(String size);

    public Film createFilm(Film film, int ratingId) { //добавляем в базу фильм с номером id рейтинга
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                ratingId
        );
        film.setId(id);
        return film;
    }

    public Optional<Film> findByID(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Film updateFilm(Film film, int ratingId) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                ratingId,
                film.getId()
        );
        return film;
    }
}
