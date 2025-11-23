package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * Модель данных фильма.
 */
@Data
public class Film {

    /**
     * Идентификатор фильма.
     */
    private Long id;

    /**
     * Название фильма.
     */
    private String name;

    /**
     * Описание фильма.
     */
    private String description;

    /**
     * Дата релиза фильма.
     */
    private LocalDate releaseDate;

    /**
     * Длительность фильма в минутах.
     */
    private int duration;
}
