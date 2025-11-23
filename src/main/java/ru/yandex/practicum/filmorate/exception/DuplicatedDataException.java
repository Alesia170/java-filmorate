package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение, возникающее при попытке указать уже существующий email.
 */
public class DuplicatedDataException extends RuntimeException {

    /**
     * Создаёт исключение с указанным сообщением.
     *
     * @param message текст ошибки.
     */
    public DuplicatedDataException(String message) {
        super(message);
    }
}
