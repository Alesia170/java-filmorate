package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

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
     * Валидатор данных пользователя.
     */
    private final UserValidator userValidator;

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
    public User create(@RequestBody User user) {
        log.info("Создание пользователя: {}", user);
        userValidator.validate(user);

        boolean emailExists = users.values()
                .stream()
                .anyMatch(exisitnigUser -> exisitnigUser.getEmail().equals(user.getEmail()));

        if (emailExists) {
            throw new DuplicatedDataException("Эта электронная почта уже используется");
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
    public User update(@RequestBody User user) {
        log.info("Обновление данных пользователя: {}", user);

        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        User oldUser = users.get(user.getId());

        if (oldUser == null) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }

        userValidator.validate(user);

        boolean emailExists = users.values()
                .stream()
                .anyMatch(exisitnigUser ->
                        exisitnigUser.getEmail().equals(user.getEmail())
                                && !exisitnigUser.getId().equals(user.getId()));

        if (emailExists) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        oldUser.setEmail(user.getEmail());
        oldUser.setBirthday(user.getBirthday());
        oldUser.setLogin(user.getLogin());
        oldUser.setName(user.getName());

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
