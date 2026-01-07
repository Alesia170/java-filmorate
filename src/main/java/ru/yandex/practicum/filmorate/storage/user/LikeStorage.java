package ru.yandex.practicum.filmorate.storage.user;

public interface LikeStorage {

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    int getLikesCount(Long filmId);
}
