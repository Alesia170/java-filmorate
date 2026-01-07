package ru.yandex.practicum.filmorate.dao.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.user.FriendsStorage;

import java.util.List;

@Repository
@Qualifier("dbFriends")
public class FriendsDbStorage implements FriendsStorage {
    private static final String ADD_FRIEND = "INSERT INTO friends (user_id, friend_id, status)" +
            "VALUES (?, ?, 'PENDING')";
    private static final String CONFIRM = "UPDATE friends SET status = 'CONFIRMED' WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_ALL_FRIENDS = "SELECT friend_id FROM friends WHERE user_id = ? ";
    private static final String FIND_COMMON_FRIENDS = "SELECT friend_id FROM friends WHERE user_id = ? " +
            "AND friend_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String DELETE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    private final JdbcTemplate jdbc;

    public FriendsDbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
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
    public List<Long> getFriends(Long userId) {
        return jdbc.queryForList(FIND_ALL_FRIENDS, Long.class, userId);
    }

    @Override
    public List<Long> getCommonFriends(Long userId, Long friendId) {
        return jdbc.queryForList(FIND_COMMON_FRIENDS, Long.class, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        jdbc.update(DELETE_FRIEND, userId, friendId);
    }
}
