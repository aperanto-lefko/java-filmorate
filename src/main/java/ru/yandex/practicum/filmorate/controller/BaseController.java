package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;

public class BaseController {

    protected final Logger log = LoggerFactory.getLogger(FilmController.class);

    protected int getNextId(Map map) {
        int currentMaxId = map.keySet()
                .stream()
                .mapToInt(id -> (int) id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    protected boolean isValueNull(String value) {
        return value == null;
    }

    protected boolean isIdNull(Integer id) {
        return id == 0;
    }

    protected boolean isDateNull(LocalDate date) {
        return date == null;
    }

    public boolean isLineBlank(String value) {
        return value.isBlank();
    }
}
