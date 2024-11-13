package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.DataReleaseValid;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Film {

    private int id;
    @NotBlank(message = "Поле с названием фильма не должно быть пустым")
    private String name;
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    private String description;
    @DataReleaseValid
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательным числом")
    private Integer duration;
    private int like;
    private Mpa mpa;
    private List<Genre> genres;
}





