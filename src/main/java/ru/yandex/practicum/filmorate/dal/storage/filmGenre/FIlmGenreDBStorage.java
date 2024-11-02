package ru.yandex.practicum.filmorate.dal.storage.filmGenre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.dal.storage.DBStorage;

public class FIlmGenreDBStorage extends DBStorage {
    public FIlmGenreDBStorage(JdbcTemplate jdbc, RowMapper<Integer> mapper) {
        super(jdbc, mapper, Integer.class);
    }


}
