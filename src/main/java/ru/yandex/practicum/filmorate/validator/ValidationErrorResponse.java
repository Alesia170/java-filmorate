package ru.yandex.practicum.filmorate.validator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Ответ, содержащий список ошибок валидации.
 */
@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {

    private final List<Violation> violations;

}
