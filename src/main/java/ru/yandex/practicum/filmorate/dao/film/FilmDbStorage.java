package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.BaseDbStorage;
import ru.yandex.practicum.filmorate.dao.mapper.FilmExtractor;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Repository
@Qualifier("dbFilm")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT f.id AS film_id, f.name AS film_name, f.description, " +
            "f.release_date, f.duration, m.id AS mpa_id, m.name AS mpa_name, g.id AS genre_id, g.name AS genre_name " +
            "FROM films AS f " +
            "LEFT JOIN mpa_rating AS m ON f.mpa_rating = m.id " +
            "LEFT JOIN film_genres AS fg ON f.id = fg.film_id " +
            "LEFT JOIN genres AS g ON fg.genre_id = g.id";
    private static final String FIND_BY_ID_QUERY = "SELECT f.id AS film_id, f.name AS film_name, f.description, " +
            "f.release_date, f.duration, m.id AS mpa_id, m.name AS mpa_name, g.id AS genre_id, g.name AS genre_name " +
            "FROM films AS f " +
            "LEFT JOIN mpa_rating AS m ON f.mpa_rating = m.id " +
            "LEFT JOIN film_genres AS fg ON f.id = fg.film_id " +
            "LEFT JOIN genres AS g ON fg.genre_id = g.id WHERE f.id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date," +
            "duration, mpa_rating) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            "duration = ?, mpa_rating = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String INSERT_FILM_GENRE = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_FILM_GENRE = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String FIND_GENRES_BY_FILM = "SELECT g.id, g.name FROM genres AS g JOIN film_genres AS fg ON " +
            "g.id = fg.genre_id WHERE fg.film_id = ? ORDER BY g.id";
    private static final String FIND_TOP_FILMS = "SELECT f.id AS film_id, f.name AS film_name, f.description, " +
            "f.release_date, f.duration, m.id AS mpa_id, m.name AS mpa_name, g.id AS genre_id, g.name AS genre_name " +
            "FROM (SELECT f.*, COUNT(DISTINCT l.user_id) AS like_count " +
            "FROM films AS f " +
            "LEFT JOIN likes AS l ON l.film_id = f.id " +
            "GROUP BY f.id " +
            "ORDER BY like_count DESC " +
            "LIMIT ? ) AS f " +
            "LEFT JOIN mpa_rating AS m ON f.mpa_rating = m.id " +
            "LEFT JOIN film_genres AS fg ON f.id = fg.film_id " +
            "LEFT JOIN genres AS g ON fg.genre_id = g.id ";

    private final RowMapper<Genre> genreMapper;
    private final FilmExtractor filmExtractor;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, RowMapper<Genre> genreMapper,
                         FilmExtractor filmExtractor) {
        super(jdbc, mapper);
        this.genreMapper = genreMapper;
        this.filmExtractor = filmExtractor;
    }

    @Override
    public List<Film> getAll() {
        return jdbc.query(FIND_ALL_QUERY, filmExtractor);
    }

    @Override
    public Optional<Film> getById(Long filmId) {
        List<Film> films = jdbc.query(FIND_BY_ID_QUERY, filmExtractor, filmId);
        return Objects.requireNonNull(films).stream().findFirst();
    }

    @Override
    public void delete(Long id) {
        boolean deleted = delete(DELETE_QUERY, id);
        if (!deleted) {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return jdbc.query(FIND_TOP_FILMS, filmExtractor, count);
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

        List<Object[]> batchArgs = film.getGenres().stream()
                .map(Genre::getId)
                .filter(Objects::nonNull)
                .distinct()
                .map(genreId -> new Object[]{film.getId(), genreId})
                .toList();

        jdbc.batchUpdate(INSERT_FILM_GENRE, batchArgs);
    }

    private LinkedHashSet<Genre> loadGenres(Long filmId) {
        return new LinkedHashSet<>(jdbc.query(FIND_GENRES_BY_FILM, genreMapper, filmId));
    }
}
