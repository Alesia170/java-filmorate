package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.Marker;
import ru.yandex.practicum.filmorate.validator.release.ValidReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Модель данных фильма.
 */
@Data
public class Film {

    /**
     * Идентификатор фильма.
     */
    @Null(groups = Marker.OnCreate.class)
    @NotNull(message = "Id должен быть указан", groups = Marker.OnUpdate.class)
    private Long id;

    /**
     * Название фильма.
     */
    @NotBlank(message = "Название фильма не может быть пустым", groups = Marker.OnCreate.class)
    private String name;

    /**
     * Описание фильма.
     */
    @Size(max = 200, message = "Максимальная длина описания 200 символов",
            groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String description;

    /**
     * Дата релиза фильма.
     */
    @ValidReleaseDate(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private LocalDate releaseDate;

    /**
     * Длительность фильма в минутах.
     */
    @Positive(message = "Продолжительность фильма должна быть положительным числом",
            groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private Integer duration;

    private Set<Long> likes = new HashSet<>();
}
