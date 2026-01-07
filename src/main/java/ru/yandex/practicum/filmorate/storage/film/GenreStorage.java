package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {

    Collection<Genre> getAll();

    Optional<Genre> getById(Long id);
}
