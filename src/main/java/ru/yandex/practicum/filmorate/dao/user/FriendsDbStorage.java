package ru.yandex.practicum.filmorate.dao.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.FriendsStorage;

import java.util.List;

@Repository
@Qualifier("dbFriends")
public class FriendsDbStorage implements FriendsStorage {
    private static final String ADD_FRIEND = "INSERT INTO friends (user_id, friend_id, status)" +
            "VALUES (?, ?, 'PENDING')";
    private static final String CONFIRM = "UPDATE friends SET status = 'CONFIRMED' WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_ALL_FRIENDS = "SELECT u.* FROM friends AS f " +
            "JOIN users AS u ON u.id = f.friend_id " +
            "WHERE f.user_id = ?";
    private static final String FIND_COMMON_FRIENDS = "SELECT u.* FROM friends AS f1 " +
            "JOIN friends AS f2 ON f1.friend_id = f2.friend_id " +
            "JOIN users AS u ON u.id = f1.friend_id " +
            "WHERE f1.user_id = ? AND f2.user_id = ? ";
    private static final String DELETE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    private final JdbcTemplate jdbc;
    private final RowMapper<User> userRowMapper;

    public FriendsDbStorage(JdbcTemplate jdbc, RowMapper<User> userRowMapper) {
        this.jdbc = jdbc;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        jdbc.update(ADD_FRIEND, userId, friendId);
    }

    @Override
    public void confirm(Long userId, Long friendId) {
        jdbc.update(CONFIRM, userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return jdbc.query(FIND_ALL_FRIENDS, userRowMapper, userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        return jdbc.query(FIND_COMMON_FRIENDS, userRowMapper, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        jdbc.update(DELETE_FRIEND, userId, friendId);
    }
}
