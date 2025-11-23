package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Модель данных пользователя.
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class User {

    /**
     * Идентификатор пользователя.
     */
    private Long id;

    /**
     * Электронная почта пользователя.
     */
    private String email;

    /**
     * Логин пользователя.
     */
    private String login;

    /**
     * Имя пользователя.
     */
    private String name;

    /**
     * День рождения пользователя.
     */
    private LocalDate birthday;
}
