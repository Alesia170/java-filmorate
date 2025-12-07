package ru.yandex.practicum.filmorate.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.validator.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.validator.Violation;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Обработчик исключений приложения.
 * Отвечает за преобразование бизнес-ошибок в корректные HTTP-ответы.
 */
@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    /**
     * Обработка ошибок валидации парамтеров
     *
     * @param e исключение ConstraintViolationException
     * @return саисок нарушений с именами параметров и сообщениями.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleConstraint(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(violation -> new Violation(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    /**
     * Обработка ошибок валидации тела запроса
     *
     * @param e исключение MethodArgumentNotValidException
     * @return список нарушений с именами полей и сообщениями.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidation(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    /**
     * Обработка ситуации, когда объект не найден
     *
     * @param e исключение NotFoundException
     * @return сообщение об ошибке
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработка ошибки дублирование данных
     *
     * @param e исключение DuplicatedDataException
     * @return сообщение об ошибке
     */
    @ExceptionHandler(DuplicatedDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicated(DuplicatedDataException e) {
        return new ErrorResponse(e.getMessage());
    }
}
