package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.dao.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({UserDbStorage.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    public void shouldFindUserById() {
        Optional<User> userOptional = userStorage.getById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void shouldReturnEmptyWhenUserNotFound() {
        Optional<User> user = userStorage.getById(10L);
        assertThat(user).isEmpty();
    }

    @Test
    public void shouldReturnAllUsers() {
        Collection<User> users = userStorage.getAll();
        assertThat(users).hasSize(2);
    }

    @Test
    public void shouldDeleteUser() {
        userStorage.delete(1L);
        assertThat(userStorage.getById(1L)).isEmpty();
    }

    @Test
    public void shouldCreateUser() {
        User user = new User();
        user.setName("test");
        user.setLogin("testLogin");
        user.setEmail("emailTest@email.com");
        user.setBirthday(LocalDate.of(2005, 1, 1));

        User created = userStorage.create(user);

        assertThat(created.getId()).isNotNull();
        assertThat(userStorage.getById(created.getId())).isPresent();
    }

    @Test
    public void shouldUpdateUser() {
        User user = new User();
        user.setName("test");
        user.setLogin("testLogin");
        user.setEmail("emailTest@email.com");
        user.setBirthday(LocalDate.of(2005, 1, 1));

        User created = userStorage.create(user);

        created.setName("changed");
        userStorage.update(created);

        Optional<User> updatedUser = userStorage.getById(created.getId());

        assertThat(updatedUser).isPresent()
                .hasValueSatisfying(u -> {
                    assertThat(u.getName()).isEqualTo("changed");
                    assertThat(u.getLogin()).isEqualTo("testLogin");
                    assertThat(u.getEmail()).isEqualTo("emailTest@email.com");
                    assertThat(u.getBirthday()).isEqualTo(LocalDate.of(2005, 1, 1));
                });
    }
}
