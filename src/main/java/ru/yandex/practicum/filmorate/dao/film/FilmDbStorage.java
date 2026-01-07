package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.BaseDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.util.*;

@Repository
@Qualifier("dbFilm")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date," +
            "duration, mpa_rating) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            "duration = ?, mpa_rating = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String INSERT_FILM_GENRE = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_FILM_GENRE = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String FIND_GENRES_BY_FILM = "SELECT g.id, g.name FROM genres AS g JOIN film_genres AS fg ON " +
            "g.id = fg.genre_id WHERE fg.film_id = ? ORDER BY g.id";

    private final MpaStorage mpaStorage;
    private final RowMapper<Genre> genreMapper;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, RowMapper<Genre> genreMapper,
                         MpaDbStorage mpaStorage) {
        super(jdbc, mapper);
        this.genreMapper = genreMapper;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = findMany(FIND_ALL_QUERY);
        for (Film film : films) {
            film.setGenres(loadGenres(film.getId()));
            film.setMpa(mpaStorage.getById(film.getMpa().getId())
                    .orElseThrow(() -> new NotFoundException("MPA не найден")));
        }
        return films;
    }

    @Override
    public Optional<Film> getById(Long filmId) {
        Optional<Film> film = findOne(FIND_BY_ID_QUERY, filmId);
        if (film.isEmpty()) {
            return Optional.empty();
        }

        Film film1 = film.get();

        film1.setGenres(loadGenres(filmId));
        mpaStorage.getById(film1.getMpa().getId())
                .ifPresent(film1::setMpa);

        return Optional.of(film1);
    }

    @Override
    public void delete(Long id) {
        boolean deleted = delete(DELETE_QUERY, id);
        if (!deleted) {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
    }

    public Film create(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        addGenres(film);
        film.setGenres(loadGenres(id));
        return film;
    }

    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        jdbc.update(DELETE_FILM_GENRE, film.getId());
        addGenres(film);
        film.setGenres(loadGenres(film.getId()));
        return film;
    }

    private void addGenres(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }

        film.getGenres().stream()
                .map(Genre::getId)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(genreId ->
                        update(INSERT_FILM_GENRE, film.getId(), genreId));
    }

    private List<Genre> loadGenres(Long filmId) {
        return new ArrayList<>(jdbc.query(FIND_GENRES_BY_FILM, genreMapper, filmId));
    }
}
