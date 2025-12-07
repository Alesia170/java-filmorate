package ru.yandex.practicum.filmorate.validator;

/**
 * Интерфейсы-маркеры для групповой валидации
 */
public interface Marker {

    /**
     * Группа валидации, применяемая для создания объекта.
     */
    interface OnCreate {}

    /**
     * Группа валидации, применяемая для обновления объекта.
     */
    interface OnUpdate {}
}
