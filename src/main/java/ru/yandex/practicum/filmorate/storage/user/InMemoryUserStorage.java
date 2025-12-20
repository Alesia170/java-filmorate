package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    /**
     * Хранилище пользователей.
     */
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return new HashSet<>(users.values());
    }

    @Override
    public User create(User user) {
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

    @Override
    public User update(User user) {
        log.info("Обновление данных пользователя: {}", user);

        User oldUser = users.get(user.getId());

        if (oldUser == null) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }

        if (user.getEmail() != null) {
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

    @Override
    public void delete(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        users.remove(id);
    }

    @Override
    public User getById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        return user;
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
