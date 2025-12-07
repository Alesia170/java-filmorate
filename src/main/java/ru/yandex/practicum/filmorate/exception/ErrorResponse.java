package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Ответ на бизнес-исключения сообщением.
 */
@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private final String message;
}
