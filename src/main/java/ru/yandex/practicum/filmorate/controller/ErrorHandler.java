package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFriendException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Objects;

@RestControllerAdvice("ru.yandex.practicum.filmorate")
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponse handleParameterNotValid(ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorResponse handleBadRequest(BadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.BAD_REQUEST) //400
   public ErrorResponse handleAnnotations(MethodArgumentNotValidException e) {
       String response = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
       log.error("Пользователь указал некорректные данные." + response);
       return new ErrorResponse("Не указаны некорректные данные. " + response);
   }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponse handleNotFound(NotFoundException e) {
        log.error("Пользователь указал неверные данные");
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponse baseNotFound(DataAccessException e) {
        log.error("Ошибка базы данных" + e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorResponse baseNotUpdate(InternalServerException e) {
        log.error("Не удалось обновить данные" + e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK) //200
    public ErrorResponse baseFriendNotFound(NotFriendException e) {
        log.error("Не найдены в базе друзей");
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponse unexpectedExceptions(Exception e) {
        log.error("Непредвиденная ошибка" + e.getMessage());
        return new ErrorResponse("Непредвиденная ошибка");
    }
   }
