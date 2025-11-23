package ru.yandex.practicum.filmorate.validator;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

/**
 * Проверяет корректность всех полей объекта {@link Film}.
 */

@Component
public final class FilmValidator {

    /**
     * Самая ранняя разрешенная дата релиза фильма.
     */
    private static final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);

    /**
     * Максимально допустимое количество символов в описании фильма.
     */
    private static final int MAX_SYMBOLS = 200;


    /**
     * Валидирует объект {@link Film}. Проверяет название, описание,
     * дату релиза и длительность фильма.
     *
     * @param film объект фильма, который необходимо проверить
     * @throws ValidationException если данные некорректны
     */
    public void validate(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription() != null
                && film.getDescription().length() > MAX_SYMBOLS) {
            throw new ValidationException("Максимальная длина описания 200 символов");
        }

        if (film.getReleaseDate() == null
                || film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
