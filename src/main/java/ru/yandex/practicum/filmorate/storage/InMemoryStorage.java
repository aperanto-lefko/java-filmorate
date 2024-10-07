package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.FilmController;

import java.time.LocalDate;

public class InMemoryStorage {

    protected final Logger log = LoggerFactory.getLogger(FilmController.class);

    protected int id = 1;

    protected int getNextId() {
        return id++;
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