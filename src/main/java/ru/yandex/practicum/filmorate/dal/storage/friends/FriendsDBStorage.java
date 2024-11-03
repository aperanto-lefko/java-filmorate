package ru.yandex.practicum.filmorate.dal.storage.friends;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.DBStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class FriendsDBStorage extends DBStorage {
    public FriendsDBStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, User.class);
    }

    //добавление в таблицу
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_user_id, status_id) VALUES (?,?,?)";
    private static final String UPDATE_FRIEND_STATUS_QUERY = "UPDATE friends SET status_id = ? WHERE user_id = ? AND friend_user_id = ?"; //проверить запрос на AND
    private static final String FIND_ID_QUERY = "SELECT id FROM friends WHERE user_id = ? AND friend_user_id = ? LIMIT 1";
    private static final String FIND_ALL_FRIEND_FOR_ID_QUERY =
            "SELECT u.*" +
                    " FROM friends f" +
                    " JOIN users u ON f.user_id = u.id" +
                    " WHERE f.friend_user_id = ? AND f.status_id = ?" +
                    " UNION" +
                    " SELECT u.*" +
                    " FROM friends f" +
                    " JOIN users u ON f.friend_user_id = u.id" +
                    " WHERE f.user_id = ? AND f.status_id = ?";


    public void insertFriend(int userId, int friendId, int status) {
        int id = insert(
                INSERT_FRIEND_QUERY,
                userId,
                friendId,
                status
        );
    }

    public void updateFriendStatus(int userId, int friendId, int status) {
        update(
                UPDATE_FRIEND_STATUS_QUERY,
                status,
                userId,
                friendId
        );
    }

    public List<User> getFriends(int userId, int status) {
        return findMany(
                FIND_ALL_FRIEND_FOR_ID_QUERY,
                userId,
                status,
                userId,
                status);
    }

    public Optional<Integer> checkFriendsInDB(int userId, int friendId) { //есть ли запись в базе по этим пользователям
        return findOne(FIND_ID_QUERY, userId, friendId);
    }
}

