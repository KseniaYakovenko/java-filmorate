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

import java.util.List;

import static ru.yandex.practicum.filmorate.controller.ErrorResponse.getErrorResponse;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        return filmService.getAll();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable long filmId) {
        return filmService.getFilmByID(filmId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Film saveFilm(@RequestBody @Valid Film film) {
        log.info("Save film: {} - Started", film);
        Film savedFilm = filmService.save(film);
        log.info("Save film: {} - Finished", savedFilm);
        return savedFilm;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody @Validated(Marker.OnUpdate.class) Film film) {
        log.info("Update film: {} - Started", film);
        Film updatedFilm = filmService.update(film);
        log.info("Update film: {} - Finished", updatedFilm);
        return updatedFilm;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        log.info("addLike for film id: {} by user: {} - Started", filmId, userId);
        filmService.addLike(userId, filmId);
        log.info("addLike for film id: {} by user: {} - Finished", filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable long filmId, @PathVariable long userId) {
        log.info("deleteLike for film id: {} by user: {} - Started", filmId, userId);
        filmService.deleteLike(userId, filmId);
        log.info("deleteLike for film id: {} by user: {} - Finished", filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam Integer count) {
        log.info("getPopular film - count={} - Started", count);
        List<Film> films = filmService.getPopular(count);
        log.info("getPopular film - count={} - Finished", count);
        return films;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(final NotFoundException e) {
        return getErrorResponse(e, log);
    }
}