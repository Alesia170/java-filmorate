package ru.yandex.practicum.filmorate;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private static Validator validator;
    FilmController controller;
    Film film;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

        controller = new FilmController();

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

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Marker.OnCreate.class);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getMessage().contains("Название фильма не может быть пустым")));
    }

    @Test
    @DisplayName("Проверка невалидности длины описания")
    void shouldThrowIfDescriptionTooLong() {
        film.setDescription("Description".repeat(90));

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Marker.OnCreate.class);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getMessage().contains("Максимальная длина описания 200 символов")));
    }

    @Test
    @DisplayName("Проверка невалидности даты релиза")
    void shouldThrowIfDateReleaseTooEarly() {
        film.setReleaseDate(LocalDate.of(1800, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Marker.OnCreate.class);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getMessage().contains("Дата релиза не может быть раньше 28 декабря 1895 года")));
    }

    @Test
    @DisplayName("Проверка невалидности длительность фильма")
    void shouldThrowIfDurationInvalid() {
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Marker.OnCreate.class);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getMessage().contains("Продолжительность фильма должна быть положительным числом")));
    }

    @Test
    @DisplayName("Обновление должно завершаться ошибкой при null значении id")
    void shouldThrowIfIdIsNull() {
        film.setId(null);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Marker.OnUpdate.class);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getMessage().contains("Id должен быть указан")));
    }

    @Test
    @DisplayName("Обновление должно завершиться ошибкой при несуществующем id")
    void shouldThrowIfIdDoesNotExist() {
        film.setId(100L);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> controller.update(film));

        assertEquals("Фильм с id = " + film.getId() + " не найден", exception.getMessage());
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
