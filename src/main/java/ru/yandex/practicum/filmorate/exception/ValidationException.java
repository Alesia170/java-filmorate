package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение, возникающее при нарушении правил валидации.
 */
public class ValidationException extends RuntimeException {

    /**
     * Создаёт исключение с указанным сообщением.
     *
     * @param message текст ошибки.
     */
    public ValidationException(String message) {
        super(message);
    }
}
