package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Component
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("dbUser") UserStorage userStorage,
                       FriendsService friendsService) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        userStorage.getById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + user.getId() + " не найден"));
        return userStorage.update(user);
    }

    public void delete(Long id) {
        userStorage.delete(id);
    }
}
