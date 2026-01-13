MERGE INTO mpa_rating (id, name)
KEY (id)
VALUES  (1, 'G'),
        (2, 'PG'),
        (3, 'PG-13'),
        (4, 'R'),
        (5, 'NC-17');

MERGE INTO genres (id, name)
KEY (id)
VALUES  (1, 'Комедия'),
        (2, 'Драма'),
        (3, 'Мультфильм'),
        (4, 'Триллер'),
        (5, 'Документальный'),
        (6, 'Боевик');

MERGE INTO users (name, email, login, birthday)
KEY(email)
VALUES('name', 'email@email.com', 'login', '2000-01-01'),
      ('name2', 'emai2l@email.com', 'login2', '2000-01-01');

INSERT INTO films (name, description, release_date, duration, mpa_rating)
VALUES('name', 'description', '2000-01-01', 129, 4),
      ('name2', 'description2', '2000-01-01', 120, 4);
