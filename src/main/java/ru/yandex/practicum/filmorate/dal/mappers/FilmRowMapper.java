package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> { //преобразование записи БД в объект и обратно
    //Метод mapRow() преобразует строку rs с порядковым номером rowNum в нужный тип.
    // Класс ResultSet обеспечивает получение результата запроса, но только для одной строки.
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getTimestamp("releaseDate").toLocalDateTime().toLocalDate()) //получили из таблицы время в timestamp и конвертировали его в LocalDate
                .duration(resultSet.getInt("duration"))
                .build();
//уточнить на каком этапе задавать like,genre,rating

    }
}
