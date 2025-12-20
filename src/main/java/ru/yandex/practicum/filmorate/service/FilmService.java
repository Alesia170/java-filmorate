package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final int DEFAULT_COUNT = 10;

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public void delete(Long id) {
        filmStorage.delete(id);
    }

    public void addLike(Long id, Long userId) {
        Film film = filmStorage.getById(id);
        User user = userStorage.getById(userId);

        film.getLikes().add(userId);
    }

    public void deleteLike(Long id, Long userId) {
        Film film = filmStorage.getById(id);
        User user = userStorage.getById(userId);

        film.getLikes().remove(userId);
    }

    public List<Film> getTopFilms(Integer count) {
        int limit;

        if (count == null || count <= 0) {
            limit = DEFAULT_COUNT;
        } else {
            limit = count;
        }

        return filmStorage.getAll().stream()
                .sorted((f1, f2) ->
                        Integer.compare(f2.getLikes().size(),
                                f1.getLikes().size()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
