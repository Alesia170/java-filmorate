package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    FilmController controller;
    Film film;

    @BeforeEach
    void setUp() {
        FilmValidator filmValidator = new FilmValidator();
        controller = new FilmController(filmValidator);

        film = new Film();
        film.setName("Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(60);
    }

    @Test
    @DisplayName("Проверка невалидности названия фильма")
    void shouldNotCreateFilmWithoutName() {
        film.setName("  ");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );

        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка невалидности длины описания")
    void shouldThrowIfDescriptionTooLong() {
        film.setDescription("Description".repeat(90));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );

        assertEquals("Максимальная длина описания 200 символов", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка невалидности даты релиза")
    void shouldThrowIfDateReleaseTooEarly() {
        film.setReleaseDate(LocalDate.of(1800, 1, 1));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );

        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка невалидности длительность фильма")
    void shouldThrowIfDurationInvalid() {
        film.setDuration(-1);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );

        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }

    @Test
    @DisplayName("Обновление должно завершаться ошибкой при null значении id")
    void shouldThrowIfIdIsNull() {
        film.setId(null);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.update(film)
        );

        assertEquals("Id должен быть указан", exception.getMessage());
    }

    @Test
    @DisplayName("Обновление должно завершиться ошибкой при несуществующем id")
    void shouldThrowIfIdDoesNotExist() {
        film.setId(100L);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> controller.update(film)
        );

        assertEquals("Фильм с id = " + film.getId() + " не найден", exception.getMessage());
    }

    @Test
    @DisplayName("Обновление должно завершиться ошибкой при некорректном имени")
    void shouldNotUpdateFilmWithoutName() {
        Film created = controller.create(film);

        Film update = new Film();
        update.setId(created.getId());
        update.setName("   ");
        update.setDescription("Description");
        update.setReleaseDate(LocalDate.now());
        update.setDuration(60);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.update(update)
        );

        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldGetAllFilms() {
        Film film1 = new Film();
        film1.setName("Film 1");
        film1.setDescription("Description 1");
        film1.setReleaseDate(LocalDate.now());
        film1.setDuration(60);

        Film film2 = new Film();
        film2.setName("Film 2");
        film2.setDescription("Description 2");
        film2.setReleaseDate(LocalDate.now());
        film2.setDuration(90);

        controller.create(film1);
        controller.create(film2);

        Collection<Film> films = controller.getAll();

        assertEquals(2, films.size());
    }
}
