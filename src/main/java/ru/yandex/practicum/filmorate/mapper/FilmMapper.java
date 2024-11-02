package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
//@NoArgsConstructor(access = AccessLevel.PRIVATE) — это аннотация из библиотеки Lombok, которая генерирует
// конструктор без аргументов для класса. Установка access = AccessLevel.PRIVATE делает этот конструктор
// приватным, что предотвращает создание экземпляров класса извне.
public class FilmMapper {
    public static FilmDto mapToFilmDto (Film film) {
         return FilmDto.builder()
                 .id(film.getId())
                 .name(film.getName())
                 .description(film.getDescription())
                 .releaseDate(film.getReleaseDate())
                 .duration(film.getDuration())
                 .rating(film.getRating())
                 .genre(film.getGenre())
                 .build();
    }
}