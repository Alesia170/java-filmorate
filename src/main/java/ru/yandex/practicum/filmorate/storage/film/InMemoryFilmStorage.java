package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    /**
     * Хранилище фильмов.
     */
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        log.info("Создание фильма: {}", film);

        film.setId(getNextId());
        films.put(film.getId(), film);

        log.info("Фильм успешно создан: id = {}", film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        log.info("Обновление фильма: {}", film);

        Film oldFilm = films.get(film.getId());
        if (oldFilm == null) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }

        if (film.getName() != null) {
            oldFilm.setName(film.getName());
        }

        if (film.getDescription() != null) {
            oldFilm.setDescription(film.getDescription());
        }

        if (film.getReleaseDate() != null) {
            oldFilm.setReleaseDate(film.getReleaseDate());
        }

        if (film.getDuration() != null) {
            oldFilm.setDuration(film.getDuration());
        }

        log.info("Фильм id={} успешно обновлен", oldFilm.getId());
        return oldFilm;
    }

    @Override
    public void delete(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
        films.remove(id);
    }

    @Override
    public Optional<Film> getById(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    /**
     * Возвращает список всех фильмов.
     *
     * @return коллекция фильмов.
     */
    @Override
    public Collection<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    /**
     * Генерирует новый уникальный идентификатор фильма.
     *
     * @return следующий id.
     */
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}