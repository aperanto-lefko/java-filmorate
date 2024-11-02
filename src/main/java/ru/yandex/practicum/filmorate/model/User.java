package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder //создаем через только через builder
public class User {

    private long id; //целочисленный идентификатор
    @Email
    @NotBlank
    private String email; // электронная почта
    @NotBlank
    private String login; //логин пользователя
    private String name; //имя для отображения
    private LocalDate birthday; //дата рождения
    //private List<Integer> listOfApplications ; //кому отправлены заявки
}
