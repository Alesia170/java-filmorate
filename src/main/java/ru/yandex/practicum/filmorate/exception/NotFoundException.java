package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение, возникающее при обращении к объекту с несуществующим id.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Создаёт исключение с указанным сообщением.
     *
     * @param message текст ошибки.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
