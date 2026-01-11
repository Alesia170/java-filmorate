package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.film.MpaDbStorage;
import ru.yandex.practicum.filmorate.dao.mapper.FilmExtractor;
import ru.yandex.practicum.filmorate.dao.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dao.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dao.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmDbStorage.class, FilmRowMapper.class, FilmExtractor.class,
        GenreRowMapper.class, MpaDbStorage.class, MpaRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    @Test
    public void shouldFindFilmById() {
        Optional<Film> filmOptional = filmStorage.getById(1L);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void shouldReturnEmptyWhenFilmNotFound() {
        Optional<Film> film = filmStorage.getById(10L);
        assertThat(film).isEmpty();
    }

    @Test
    public void shouldReturnAllFilms() {
        Collection<Film> films = filmStorage.getAll();
        assertThat(films).hasSize(2);
    }

    @Test
    public void shouldDeleteFilm() {
        filmStorage.delete(1L);
        assertThat(filmStorage.getById(1L)).isEmpty();
    }

    @Test
    public void shouldCreateFilm() {
        Film film = new Film();
        Mpa mpa = new Mpa();
        mpa.setId(4L);
        film.setName("test");
        film.setDescription("testDescription");
        film.setReleaseDate(LocalDate.of(2005, 1, 1));
        film.setDuration(120);
        film.setMpa(mpa);

        Film created = filmStorage.create(film);

        assertThat(created.getId()).isNotNull();
        assertThat(filmStorage.getById(created.getId())).isPresent();
    }

    @Test
    public void shouldUpdateFilm() {
        Film film = new Film();
        Mpa mpa = new Mpa();
        mpa.setId(2L);
        film.setName("test");
        film.setDescription("testDescription");
        film.setReleaseDate(LocalDate.of(2005, 1, 1));
        film.setDuration(120);
        film.setMpa(mpa);

        Film created = filmStorage.create(film);

        created.setName("changed");
        filmStorage.update(created);

        Optional<Film> updatedFilm = filmStorage.getById(created.getId());

        assertThat(updatedFilm).isPresent()
                .hasValueSatisfying(u -> {
                    assertThat(u.getName()).isEqualTo("changed");
                    assertThat(u.getDescription()).isEqualTo("testDescription");
                    assertThat(u.getReleaseDate()).isEqualTo(LocalDate.of(2005, 1, 1));
                    assertThat(u.getDuration()).isEqualTo(120);
                    assertThat(u.getMpa().getId()).isEqualTo(2L);
                });
    }
}
