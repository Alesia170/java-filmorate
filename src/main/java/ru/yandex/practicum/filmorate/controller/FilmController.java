package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для операций с фильмами.
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    /**
     * Хранилище фильмов.
     */
    private final Map<Long, Film> films = new HashMap<>();

    /**
     * Возвращает список всех фильмов.
     *
     * @return коллекция фильмов.
     */
    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    /**
     * Создаёт новый фильм.
     *
     * @param film объект фильма
     * @return созданный фильм.
     */
    @PostMapping
    public Film create(@RequestBody @Validated(Marker.OnCreate.class) Film film) {
        log.info("Создание фильма: {}", film);

        film.setId(getNextId());
        films.put(film.getId(), film);

        log.info("Фильм успешно создан: id = {}", film.getId());
        return film;
    }

    /**
     * Обновляет данные существующего фильма.
     *
     * @param film обновлённые данные фильма
     * @return обновлённый фильм.
     */
    @PutMapping
    public Film update(@RequestBody @Validated(Marker.OnUpdate.class) Film film) {
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
