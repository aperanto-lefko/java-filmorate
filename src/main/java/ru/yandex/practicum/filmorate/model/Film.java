package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Film {

    private int id; //целочисленный идентификатор
    @NotBlank(message = "Поле с названием фильма не должно быть пустым")
    private String name; //название
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    private String description; //описание
    private LocalDate releaseDate; //дата релиза
    @Positive(message = "Продолжительность не может быть отрицательным числом")
    private Integer duration; //продолжительность фильма
    private int like;
    /*private int mpa;
    private List<Integer> genres;*/
    private Mpa mpa; //рейтинг
    private List<Genre> genres;
}





