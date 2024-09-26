package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id; //целочисленный идентификатор
    @Email
    private String email; // электронная почта
    @NotBlank
    private String login; //логин пользователя
    private String name; //имя для отображения
    private LocalDate birthday; //дата рождения
}
