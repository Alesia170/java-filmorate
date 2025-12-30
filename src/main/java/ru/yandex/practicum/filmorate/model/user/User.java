package ru.yandex.practicum.filmorate.model.user;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Модель данных пользователя.
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class User {

    /**
     * Идентификатор пользователя.
     */
    @Null(groups = Marker.OnCreate.class)
    @NotNull(message = "Id должен быть указан", groups = Marker.OnUpdate.class)
    private Long id;

    /**
     * Электронная почта пользователя.
     */
    @NotBlank(message = "Электронная почта не может быть пустой", groups = Marker.OnCreate.class)
    @Email(message = "Неверный формат email",
            groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String email;

    /**
     * Логин пользователя.
     */
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы", groups = Marker.OnCreate.class)
    private String login;

    /**
     * Имя пользователя.
     */
    private String name;

    /**
     * День рождения пользователя.
     */
    @PastOrPresent(message = "Дата рождения не может быть в будущем",
            groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    private Map<Long, FriendshipStatus> status;
}
