package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.util.Collection;
import java.util.List;

/**
 * Контроллер для операций с фильмами.
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    /**
     * Возвращает список всех фильмов.
     *
     * @return коллекция фильмов.
     */
    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    /**
     * Создаёт новый фильм.
     *
     * @param film объект фильма
     * @return созданный фильм.
     */
    @PostMapping
    public Film create(@RequestBody @Validated(Marker.OnCreate.class) Film film) {
        return filmService.create(film);
    }

    /**
     * Обновляет данные существующего фильма.
     *
     * @param film обновлённые данные фильма
     * @return обновлённый фильм.
     */
    @PutMapping
    public Film update(@RequestBody @Validated(Marker.OnUpdate.class) Film film) {
        return filmService.update(film);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        filmService.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id,
                        @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id,
                           @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(required = false) Integer count) {
        return filmService.getTopFilms(count);
    }
}
