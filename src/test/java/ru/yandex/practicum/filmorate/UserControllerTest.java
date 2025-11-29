package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private static Validator validator;
    UserController controller;
    User user;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

        controller = new UserController();

        user = new User();
        user.setEmail("@Email");
        user.setName("Name");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(2000, 10, 12));
    }

    @Test
    @DisplayName("Проверка невалидности электронного адреса")
    void shouldNotCreateEmailEmpty() {
        user.setEmail("  ");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.OnCreate.class);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getMessage()
                        .contains("Электронная почта не может быть пустой")));
    }

    @Test
    @DisplayName("Проверка на существование электронного адреса")
    void shouldThrowIfEmailExists() {
        controller.create(user);

        User user2 = new User();
        user2.setEmail("@Email");
        user2.setName("Name2");
        user2.setLogin("Login2");
        user2.setBirthday(LocalDate.of(2000, 10, 12));

        DuplicatedDataException exception = assertThrows(
                DuplicatedDataException.class,
                () -> controller.create(user2)
        );

        assertEquals("Эта электронная почта уже используется", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка невалидности логина пользователя")
    void shouldThrowIfLoginEmpty() {
        user.setLogin("   ");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.OnCreate.class);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getMessage()
                        .contains("Логин не может быть пустым и содержать пробелы")));
    }

    @Test
    @DisplayName("Пустое имя пользователя заменяется логином")
    void shouldReplaceEmptyNameWithLogin() {
        user.setName("  ");

        User user1 = controller.create(user);

        assertEquals(user1.getLogin(), user1.getName());
    }

    @Test
    @DisplayName("Проверка невалидности даты рождения")
    void shouldThrowIdDateBirthdayFromFuture() {
        user.setBirthday(LocalDate.of(2030, 10, 11));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.OnCreate.class);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getMessage()
                        .contains("Дата рождения не может быть в будущем")));
    }

    @Test
    @DisplayName("Обновление должно завершаться ошибкой при null значении id")
    void shouldThrowIfIdIsNull() {
        user.setId(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.OnUpdate.class);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getMessage()
                        .contains("Id должен быть указан")));
    }

    @Test
    @DisplayName("Обновление должно завершиться ошибкой при несуществующем id")
    void shouldThrowIfIdDoesNotExist() {
        user.setId(100L);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> controller.update(user)
        );

        assertEquals("Пользователь с id = " + user.getId() + " не найден", exception.getMessage());
    }

    @Test
    @DisplayName("Создание email должно завершиться ошибкой при некорректной почте")
    void shouldThrowIfEmailWrong() {
        user.setEmail("это-неправильный?эмейл@");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.OnCreate.class);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getMessage()
                        .contains("Неверный формат email")));
    }

    @Test
    void shouldGetAllUsers() {
        User user1 = new User();
        user1.setEmail("@email");
        user1.setLogin("login");
        user1.setName("name");
        user.setBirthday(LocalDate.of(2000, 12, 12));

        User user2 = new User();
        user2.setEmail("@email12");
        user2.setLogin("login");
        user2.setName("name");
        user.setBirthday(LocalDate.of(2000, 10, 10));

        controller.create(user1);
        controller.create(user2);

        Collection<User> users = controller.getAll();

        assertEquals(2, users.size());
    }
}
