package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FriendsService {

    private final FriendsStorage friendsStorage;
    private final UserStorage userStorage;

    public FriendsService(@Qualifier("dbFriends") FriendsStorage friendsStorage,
                          @Qualifier("dbUser") UserStorage userStorage) {
        this.friendsStorage = friendsStorage;
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        validateUsers(userId, friendId);

        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Нельзя добавить себя в друзья");
        }

        friendsStorage.addFriend(userId, friendId);
    }

    public void confirm(Long userId, Long friendId) {
        validateUsers(userId, friendId);
        friendsStorage.confirm(friendId, userId);
    }

    public List<User> getFriends(Long userId) {
        validateUser(userId);
        return friendsStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        validateUsers(userId, friendId);
        return friendsStorage.getCommonFriends(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        validateUsers(userId, friendId);
        friendsStorage.deleteFriend(userId, friendId);
    }

    private void validateUsers(Long userId, Long friendId) {
        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id= " + userId + " не найден"));
        userStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + friendId + " не найден"));
    }

    private void validateUser(Long userId) {
        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
