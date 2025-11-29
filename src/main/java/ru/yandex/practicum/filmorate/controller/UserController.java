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
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для операций с пользователями.
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    /**
     * Хранилище пользователей.
     */
    private final Map<Long, User> users = new HashMap<>();

    /**
     * Возвращает список всех пользователей.
     *
     * @return коллекция пользователей.
     */
    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    /**
     * Создаёт нового пользователя.
     *
     * @param user объект пользователя
     * @return созданный пользователь.
     */
    @PostMapping
    public User create(@RequestBody @Validated(Marker.OnCreate.class) User user) {
        log.info("Создание пользователя: {}", user);

        boolean emailExists = users.values()
                .stream()
                .anyMatch(exisitnigUser -> exisitnigUser.getEmail().equals(user.getEmail()));

        if (emailExists) {
            throw new DuplicatedDataException("Эта электронная почта уже используется");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь успешно создан: id = {}", user.getId());
        return user;
    }

    /**
     * Обновляет данные существующего пользователя.
     *
     * @param user изменённый пользователь
     * @return обновлённый пользователь.
     */
    @PutMapping
    public User update(@RequestBody @Validated(Marker.OnUpdate.class) User user) {
        log.info("Обновление данных пользователя: {}", user);

        User oldUser = users.get(user.getId());

        if (oldUser == null) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }

        if (oldUser.getEmail() != null) {
            boolean emailExists = users.values()
                    .stream()
                    .anyMatch(exisitnigUser ->
                            exisitnigUser.getEmail().equals(user.getEmail())
                                    && !exisitnigUser.getId().equals(user.getId()));

            if (emailExists) {
                throw new DuplicatedDataException("Этот email уже используется");
            }

            oldUser.setEmail(user.getEmail());
        }

        if (user.getLogin() != null) {
            oldUser.setLogin(user.getLogin());
        }

        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }

        if (user.getBirthday() != null) {
            oldUser.setBirthday(user.getBirthday());
        }

        log.info("Обновление пользователя id={} прошло успешно", oldUser.getId());
        return oldUser;
    }

    /**
     * Генерирует новый уникальный идентификатор.
     *
     * @return id.
     */
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
