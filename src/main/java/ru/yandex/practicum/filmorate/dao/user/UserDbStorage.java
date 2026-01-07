package ru.yandex.practicum.filmorate.dao.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.BaseDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
@Qualifier("dbUser")
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(name, email, login, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET name = ?, email = ?, login = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<User> getById(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    @Override
    public void delete(Long id) {
        boolean deleted = delete(DELETE_QUERY, id);
        if (!deleted) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
    }

    public User create(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getId()
        );
        return user;
    }
}
