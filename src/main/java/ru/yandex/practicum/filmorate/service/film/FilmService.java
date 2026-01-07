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
        mpaStorage.getById(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("MPA с id=" + film.getId() + " не найден"));

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.getById(genre.getId())
                        .orElseThrow(() -> new NotFoundException("Жанр с id =" + genre.getId() + " не найден"));
            }
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        filmStorage.getById(film.getId())
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + film.getId() + " не найден"));

        mpaStorage.getById(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("MPA с id=" + film.getId() + " не найден"));

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.getById(genre.getId())
                        .orElseThrow(() -> new NotFoundException("Жанр с id =" + genre.getId() + " не найден"));
            }
        }

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
        return filmStorage.getAll().stream()
                .sorted((f1, f2) ->
                        Integer.compare(likeStorage.getLikesCount(f2.getId()),
                                likeStorage.getLikesCount(f1.getId())))
                .limit(count)
                .collect(Collectors.toList());
    }
}