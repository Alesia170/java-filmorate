package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
@Qualifier("dbMpa")
public class MpaDbStorage extends BaseDbStorage<Mpa> implements MpaStorage {
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM mpa_rating ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM mpa_rating WHERE id = ?";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Mpa> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Mpa> getById(Long mpaId) {
        return findOne(FIND_BY_ID_QUERY, mpaId);
    }
}
