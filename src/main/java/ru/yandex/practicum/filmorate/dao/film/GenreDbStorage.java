package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
@Qualifier("dbGenre")
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM genres ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM genres WHERE id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Genre> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Genre> getById(Long genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }
}
