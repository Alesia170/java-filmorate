package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    void delete(Long id);

    Optional<Film> getById(Long id);

    Collection<Film> getAll();
}
