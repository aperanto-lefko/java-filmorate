package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;


    public class ErrorResponse {
        @Getter
        String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
    }

