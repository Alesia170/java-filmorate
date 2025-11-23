package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    UserController controller;
    User user;

    @BeforeEach
    void setUp() {
        UserValidator userValidator = new UserValidator();
        controller = new UserController(userValidator);

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

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
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

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );

        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
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

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );

        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    @DisplayName("Обновление должно завершаться ошибкой при null значении id")
    void shouldThrowIfIdIsNull() {
        user.setId(null);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.update(user)
        );

        assertEquals("Id должен быть указан", exception.getMessage());
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
    @DisplayName("Обновление должно завершиться ошибкой при некорректной электронной почты")
    void shouldNotUpdateUserWithoutEmail() {
        User created = controller.create(user);

        User update = new User();
        update.setId(created.getId());
        update.setEmail("   ");
        update.setLogin("Login");
        update.setName("Name");
        update.setBirthday(LocalDate.of(2000, 12, 12));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.update(update)
        );

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
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
