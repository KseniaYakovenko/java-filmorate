package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.slf4j.Logger;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;

@Data
public class ErrorResponse {
    private String message;
    @JsonIgnore
    private String stacktrace;

    public ErrorResponse(String message) {
        this.message = message;
    }

    static ErrorResponse getErrorResponse(NotFoundException e, Logger log) {
        log.info("Error", e);
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        errorResponse.setStacktrace(pw.toString());
        return errorResponse;
    }
}