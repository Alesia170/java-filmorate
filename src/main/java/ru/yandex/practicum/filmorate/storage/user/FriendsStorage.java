package ru.yandex.practicum.filmorate.storage.user;

import java.util.List;

public interface FriendsStorage {

    void addFriend(Long userId, Long friendId);

    void confirm(Long userId, Long friendId);

    List<Long> getFriends(Long userId);

    List<Long> getCommonFriends(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);
}
