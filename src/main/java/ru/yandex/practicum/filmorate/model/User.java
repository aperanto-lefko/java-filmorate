package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.DataFutureValid;
import ru.yandex.practicum.filmorate.validation.LoginValid;

import java.time.LocalDate;


@Data
@Builder
public class User {

    private int id;
    @Email
    @NotBlank
    private String email;
    @LoginValid
    private String login;
    private String name;
    @DataFutureValid
    private LocalDate birthday;
}
