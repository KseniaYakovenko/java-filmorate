package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Marker;

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
}