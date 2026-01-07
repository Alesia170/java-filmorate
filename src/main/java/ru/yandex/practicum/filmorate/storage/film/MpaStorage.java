package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.Collection;
import java.util.Optional;

public interface MpaStorage {

    Collection<Mpa> getAll();

    Optional<Mpa> getById(Long id);
}
