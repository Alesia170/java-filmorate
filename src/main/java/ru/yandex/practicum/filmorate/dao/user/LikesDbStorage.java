package ru.yandex.practicum.filmorate.dao.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.user.LikeStorage;

@Repository
@Qualifier("dbLikes")
public class LikesDbStorage implements LikeStorage {
    private static final String ADD_LIKE = "INSERT INTO likes (film_id, user_Id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String COUNT_LIKES = "SELECT COUNT(*) FROM likes WHERE film_id = ?";

    private final JdbcTemplate jdbc;

    public LikesDbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        jdbc.update(ADD_LIKE, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        jdbc.update(DELETE_LIKE, filmId, userId);
    }

    @Override
    public int getLikesCount(Long filmId) {
        return jdbc.queryForObject(COUNT_LIKES, Integer.class, filmId);
    }
}
