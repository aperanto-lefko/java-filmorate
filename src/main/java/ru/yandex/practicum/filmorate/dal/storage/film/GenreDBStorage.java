package ru.yandex.practicum.filmorate.dal.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.DBStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Repository
public class GenreDBStorage extends DBStorage {
    public GenreDBStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper, Genre.class);
    }

    private static final String GENRE_QUERY = "SELECT * FROM genre WHERE id = ?";
    private static final String ALL_GENRE_QUERY = "SELECT * FROM genre";


    public Optional<Genre> getGenreById(int id) {
        return findOne(GENRE_QUERY, mapper, id);
    }

    public List<Genre> getListGenre(List<Genre> list) { //передача списка жанров в соотв.сос писком полученных id
        String placeholders = String.join(",", Collections.nCopies(list.size(), "?"));//ставим столько вопросов, сколько параметров в list
        String LIST_GENRE_QUERY = "SELECT * FROM genre WHERE id IN (" + placeholders + ")";
        List<Integer> listInt = list.stream() //собираем список id, чтобы передать в метод
                .map(Genre::getId)
                .toList();
        Object[] params = listInt.toArray(new Object[0]);
        return findMany(LIST_GENRE_QUERY, params);
    }
    public List<Genre> getAllGenre() {
        return findMany(ALL_GENRE_QUERY);
    }

    

}
