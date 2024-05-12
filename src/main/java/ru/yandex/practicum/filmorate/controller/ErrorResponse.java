package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    @JsonIgnore
    private String stacktrace;

    public ErrorResponse(String message) {
        this.message = message;
    }
}