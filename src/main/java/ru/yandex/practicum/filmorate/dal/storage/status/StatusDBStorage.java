package ru.yandex.practicum.filmorate.dal.storage.status;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.DBStorage;

import java.util.Optional;

@Repository
public class StatusDBStorage extends DBStorage
{
    public StatusDBStorage (JdbcTemplate jdbc, RowMapper<Integer> mapper) {
        super(jdbc, mapper, Integer.class);
    }
    private static final String STATUS_ID_QUERY = "SELECT id FROM status WHERE name = ?";
    public Optional<Integer> getStatusId(String status) {
        return findOne(STATUS_ID_QUERY, status);
    }
}
