package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    List<Film> getAllFilms() {
        return filmService.getAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    Film saveFilm(@RequestBody @Valid Film film) {
        log.info("Update film: {} - Started", film);
        Film savedFilm = filmService.save(film);
        log.info("Update film: {} - Finished", savedFilm);
        return savedFilm;
    }

    @PutMapping()
    Film updateFilm(@RequestBody @Validated(Marker.OnUpdate.class) Film film) {
        log.info("Update film: {} - Started", film);
        Film updatedFilm = filmService.update(film);
        log.info("Update film: {} - Finished", updatedFilm);
        return updatedFilm;
    }

    @PutMapping("/{filmId}/like/{userId}")
    void addLike(@PathVariable long filmId, @PathVariable long userId) {
        log.info("addLike for film id: {} by user: {} - Started", filmId, userId);
        filmService.addLike(userId, filmId);
        log.info("addLike for film id: {} by user: {} - Finished", filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    void deleteLike(@PathVariable long filmId, @PathVariable long userId) {
        log.info("deleteLike for film id: {} by user: {} - Started", filmId, userId);
        filmService.deleteLike(userId, filmId);
        log.info("deleteLike for film id: {} by user: {} - Finished", filmId, userId);
    }

    @GetMapping("/popular")
    List<Film> getPopular(@RequestParam Integer count) {
        log.info("getPopular film - count={} - Started", count);
        List<Film> films = filmService.getPopular(count);
        log.info("getPopular film - count={} - Finished", count);
        return films;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(final NotFoundException e) {
        log.info("Error", e);
        ErrorResponse errorResponse = new ru.yandex.practicum.filmorate.controller.ErrorResponse(e.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        errorResponse.setStacktrace(pw.toString());
        return errorResponse;
    }
}