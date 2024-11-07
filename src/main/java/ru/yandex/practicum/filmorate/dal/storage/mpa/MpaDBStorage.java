package ru.yandex.practicum.filmorate.dal.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.DBStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDBStorage extends DBStorage {

    public MpaDBStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper, Mpa.class);
    }

    private static final String FIND_All_QUERY = "SELECT * FROM rating";
    private static final String FIND_MPA_BY_ID_QUERY = "SELECT * FROM rating WHERE id=?";

    public List<Mpa> getAllMpa() {
        return findMany(FIND_All_QUERY);
    }

    public Optional<Mpa> findMpaByID(int id) {
        return findOne(FIND_MPA_BY_ID_QUERY, mapper, id);
    }
}