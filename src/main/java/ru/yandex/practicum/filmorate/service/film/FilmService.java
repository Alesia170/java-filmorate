package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.user.LikeStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public FilmService(@Qualifier("dbFilm") FilmStorage filmStorage,
                       @Qualifier("dbUser") UserStorage userStorage,
                       @Qualifier("dbLikes") LikeStorage likeStorage,
                       @Qualifier("dbMpa") MpaStorage mpaStorage,
                       @Qualifier("dbGenre") GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Long id) {
        return filmStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    public Film create(Film film) {
        validate(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validate(film);
        return filmStorage.update(film);
    }

    public void delete(Long id) {
        filmStorage.delete(id);
    }

    public void addLike(Long id, Long userId) {
        filmStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден")
                );

        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден")
                );

        likeStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        filmStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден")
                );

        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден")
                );

        likeStorage.deleteLike(id, userId);
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);
    }

    private void validate(Film film) {
        mpaStorage.getById(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("MPA с id=" + film.getMpa().getId() + " не найден"));

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }

        Set<Long> existingGenreIds = genreStorage.getAll().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        for (Genre genre : film.getGenres()) {
            if (!existingGenreIds.contains(genre.getId())) {
                throw new NotFoundException("Жанр с id =" + genre.getId() + " не найден");
            }
        }
    }
}