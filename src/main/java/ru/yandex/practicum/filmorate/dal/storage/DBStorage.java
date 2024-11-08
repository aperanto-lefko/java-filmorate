package ru.yandex.practicum.filmorate.dal.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class DBStorage<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;
    private final Class<T> entityType; //проверить работает без этого или нет

    protected Optional<T> findOne(String query, RowMapper<T> mapper, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        } catch (DataAccessException e) {
            log.error("Ошибка доступа к данным: ", e);
            throw e;
        }
    }

    protected List<T> findMany(String query, Object... params) {
        try {
            return jdbc.query(query, mapper, params);
        } catch (DataAccessException e) {
            log.error("Ошибка доступа к данным: ", e);
            throw e;
        }
    }

    /*
    метод с переменным количеством аргументов, объявленный с использованием синтаксиса Object..., позволяет передавать
     неограниченное количество аргументов (или не передавать их вовсе). Это называется "varargs"
      (variable-length arguments). Если вы объявляете метод как findMany(String query, Object... params), вы можете:
    1. Не передавать никаких параметров: В этом случае params будет представлять пустой массив.
    2. Передать любой набор параметров: Вы можете передать любое количество объектов, включая ноль.
     */
    protected boolean delete(String query, long id) {
        int rowsDeleted = jdbc.update(query, id);
        return rowsDeleted > 0;
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");

        }
    }

    protected int insert(String query, Object... params) {
        // Создаем объект для хранения сгенерированного ключа (ID)
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        // Выполняем обновление в базе данных, используя предоставленный SQL-запрос
        jdbc.update(connection -> {
            // Подготавливаем SQL-запрос с возможностью получения сгенерированных ключей
            //Соединение (объект connection) предоставляет JdbcTemplate
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); //статус вернуть сгенерированные ключи
            // Устанавливаем параметры в подготовленный запрос
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            // Возвращаем подготовленный запрос
            return ps;
        }, keyHolder); // Передаем keyHolder для получения сгенерированного ключа

        Integer id = keyHolder.getKeyAs(Integer.class);
        //Long id = keyHolder.getKeyAs(Long.class); //в начальном варианте было long

        // Возвращаем id нового пользователя
        if (id != null) {

            // Если ID не равен null, возвращаем его
            return id;
        } else {
            // Если ID равен null, выбрасываем исключение, так как сохранение данных не удалось
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    protected int insertMany(String query) {
        int count = jdbc.update(query);
        if (count > 0) {
            return count;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }
}
