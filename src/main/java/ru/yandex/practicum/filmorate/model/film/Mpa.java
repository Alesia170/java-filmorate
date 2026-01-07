package ru.yandex.practicum.filmorate.model.film;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Mpa {

    private Long id;
    private String name;
}
