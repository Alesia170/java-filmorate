package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.util.Collection;

@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    public MpaService(@Qualifier("dbMpa") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Collection<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    public Mpa getById(Long id) {
        return mpaStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("MPA c id=" + id + " не найден"));
    }
}
