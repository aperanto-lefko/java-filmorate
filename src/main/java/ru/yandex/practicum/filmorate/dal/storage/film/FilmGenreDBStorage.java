package ru.yandex.practicum.filmorate.dal.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.DBStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
public class FilmGenreDBStorage extends DBStorage {

    public FilmGenreDBStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper, Genre.class);
    }

    private static final String GENRE_FOR_FILM_QUERY =
            "SELECT g.* FROM filmGenre f JOIN genre g ON f.genre_id=g.id WHERE f.film_id = ?";

    public List<Genre> getGenreForFilm(int id) {
        return findMany(GENRE_FOR_FILM_QUERY, id);
    }

    public void insertGenreForFilm(int idFilm, List<Genre> list) {
        List<Integer> listInt = list.stream() //собираем список id, чтобы передать в метод
                .map(Genre::getId)
                .toList();
        StringBuilder valuesBuilder = new StringBuilder();
        for (int idGenre : listInt) {
            if (valuesBuilder.length() > 0) {
                valuesBuilder.append(", ");
            }
            valuesBuilder.append("(").append(idFilm).append(", ").append(idGenre).append(")");
        }
        String INSERT_GENRE_FOR_FILM = "INSERT INTO filmGenre (film_id, genre_id) VALUES " + valuesBuilder;
        insertMany(INSERT_GENRE_FOR_FILM);
    }

}
/*

//результат  несколько значений в формате `(film_id, genre_id)`, разделенных запятыми
(1, 2), (1, 3), (1, 4)

в H2 можно применить формат
INSERT INTO filmGenre (film_id, genre_id) VALUES
(1, 2),
(1, 3),
(1, 4);
 */