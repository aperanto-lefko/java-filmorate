package ru.yandex.practicum.filmorate.dal.storage.rating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.DBStorage;
import ru.yandex.practicum.filmorate.model.Rating;


import java.util.Optional;

@Repository
public class RatingDBStorage extends DBStorage {

    public RatingDBStorage (JdbcTemplate jdbc, RowMapper<Integer> mapper) {
        super(jdbc, mapper);
    }
    private static final String RATING_ID_QUERY = "SELECT id FROM rating WHERE name = ?";

    public Optional<Integer> getRatingId(Rating rating) {
        return findOne(RATING_ID_QUERY, rating.toString());
    }
}
