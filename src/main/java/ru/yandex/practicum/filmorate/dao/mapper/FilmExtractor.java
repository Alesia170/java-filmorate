package ru.yandex.practicum.filmorate.dao.mapper;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class FilmExtractor implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet resultSet) throws SQLException {
        Map<Long, Film> films = new LinkedHashMap<>();

        while (resultSet.next()) {
            long filmId = resultSet.getLong("film_id");

            Film film = films.get(filmId);
            if (film == null) {
                film = new Film();
                film.setId(filmId);
                film.setName(resultSet.getString("film_name"));
                film.setDescription(resultSet.getString("description"));
                film.setReleaseDate(resultSet.getObject("release_date", LocalDate.class));
                film.setDuration(resultSet.getInt("duration"));
                Mpa mpa = new Mpa(resultSet.getLong("mpa_id"),
                        resultSet.getString("mpa_name"));
                film.setMpa(mpa);

                film.setGenres(new LinkedHashSet<>());
                films.put(filmId, film);
            }

            String genreName = resultSet.getString("genre_name");

            if (genreName != null) {
                Long genreId = resultSet.getLong("genre_id");
                film.getGenres().add(new Genre(genreId, genreName));
            }
        }

        return new ArrayList<>(films.values());
    }
}
