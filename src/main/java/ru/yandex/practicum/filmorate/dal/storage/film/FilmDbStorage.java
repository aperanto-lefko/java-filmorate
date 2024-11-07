package ru.yandex.practicum.filmorate.dal.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.DBStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository("FilmDBStorage")
//аннотация для класса, чтобы добавлялись в контекст, перехват исключений, которые потом преобразовывает в стандартные исключения Spring — DataAccessException
public class FilmDbStorage extends DBStorage implements FilmStorage { //слой DAO логика работы с таблицей film


    private static final String FIND_All_QUERY = "SELECT * FROM films";

    private static final String INSERT_QUERY =
            "INSERT INTO films (name, description, releaseDate, duration, rating_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
       private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";

    private static final String UPDATE_QUERY =
            "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?," +
                    " rating_id = ? WHERE id = ?";

    private static final String POPULAR_FILM_QUERY =
           "SELECT f.*" +
                   " FROM films f" +
                   " JOIN (" +
                   " SELECT film_id, COUNT(like_user_id) AS like_count" +
                   " FROM likes" +
                   " GROUP BY film_id" +
                   " ORDER BY like_count DESC) l ON f.id=l.film_id LIMIT ?";



    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper, Film.class);
    }

    public List<Film> getAllFilms() {
        return findMany(FIND_All_QUERY);
    }


    public List<Film> popularFilm(String count) { ///чтобы сохранить метод в инт. оставим String
        return findMany(POPULAR_FILM_QUERY,Integer.parseInt(count));
    }

    public Film createFilm(Film film) { //добавляем в базу фильм с номером id рейтинга
        int id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        return film;
    }

    public Optional<Film> findFilmByID(int id) {
        return findOne(FIND_BY_ID_QUERY, mapper, id);
    }
    public Film updateFilm(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

}
