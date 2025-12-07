package ru.yandex.practicum.filmorate.validator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Представляет одну ошибку валидации.
 * Содержит имя поля и сообщение об ошибке.
 */
@Getter
@RequiredArgsConstructor
public class Violation {

    private final String fieldName;
    private final String message;
}
