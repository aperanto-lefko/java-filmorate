package ru.yandex.practicum.filmorate.dal.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.dal.storage.DBStorage;

import java.util.Optional;

public class GenreDBStorage extends DBStorage {
    public GenreDBStorage(JdbcTemplate jdbc, RowMapper<Integer> mapper) {
        super(jdbc, mapper, Integer.class);
    }

    private static final String GENRE_ID_QUERY = "SELECT id FROM genre WHERE name = ?";

    public Optional<Integer> getGenreId(String genre) {
        return findOne(GENRE_ID_QUERY, genre);
    }
}

