package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmorateApplicationTests {
    static FilmController filmController = new FilmController();
static UserController userController = new UserController();
    @Test
    public void validateFilmDate() {
        Film validFilm = new Film();
        validFilm.setReleaseDate(LocalDate.of(1794, 12, 5));
        Exception e = assertThrows(ValidationException.class, () -> filmController.checkDate(validFilm),
                "Метод работает некорректно");
        assertTrue(e.getMessage().contains("Дата релиза не может быть раньше 28.12.1985"));
    }
@Test
   public void validateUserLogin() {
        User validUser = new User();
        validUser.setLogin("name user");
        Exception e = assertThrows(ValidationException.class,() -> userController.checkLoginField(validUser),
                "Метод работает некорректно");
        assertTrue(e.getMessage().contains("Поле логин не может содержать пробелы"));
}


}
