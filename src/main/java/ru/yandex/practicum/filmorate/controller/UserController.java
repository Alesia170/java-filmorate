package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.util.Collection;
import java.util.List;

/**
 * Контроллер для операций с пользователями.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Возвращает список всех пользователей.
     *
     * @return коллекция пользователей.
     */
    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }

    /**
     * Создаёт нового пользователя.
     *
     * @param user объект пользователя
     * @return созданный пользователь.
     */
    @PostMapping
    public User create(@RequestBody @Validated(Marker.OnCreate.class) User user) {
        return userService.create(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    /**
     * Обновляет данные существующего пользователя.
     *
     * @param user изменённый пользователь
     * @return обновлённый пользователь.
     */
    @PutMapping
    public User update(@RequestBody @Validated(Marker.OnUpdate.class) User user) {
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id,
                                       @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
