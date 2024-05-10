package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {
    private String message;
    @JsonIgnore
    private String stacktrace;

    public ErrorResponse(String message) {
        this.message = message;
    }
}