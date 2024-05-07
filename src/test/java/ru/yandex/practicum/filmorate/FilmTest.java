package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void validateEmptyName() {
        String emptyName = "";
        Film film = new Film(1L, emptyName, "description", LocalDate.now(), 5);
        Set<ConstraintViolation<Film>> violation = validator.validate(film);
        assertEquals(1, violation.size());
    }

    @Test
    void validateNotEmptyName() {
        String notEmptyName = "name";
        Film film = new Film(1L, notEmptyName, "description", LocalDate.now(), 5);
        Set<ConstraintViolation<Film>> violation = validator.validate(film);
        assertTrue(violation.isEmpty());
    }

    @Test
    void validateFailReleaseDate() {
        LocalDate failReleaseDate = LocalDate.of(1000, 1, 1);
        Film film = new Film(1L, "name", "description", failReleaseDate, 5);
        Set<ConstraintViolation<Film>> violation = validator.validate(film);
        assertEquals(1, violation.size());
    }

    @Test
    void validateReleaseDate() {
        LocalDate failReleaseDate = LocalDate.of(2000, 1, 1);
        Film film = new Film(1L, "name", "description", failReleaseDate, 5);
        Set<ConstraintViolation<Film>> violation = validator.validate(film);
        assertTrue(violation.isEmpty());
    }

    @Test
    void validateFailDuration() {
        Integer duration = -100;
        Film film = new Film(1L, "name", "description", LocalDate.now(), duration);
        Set<ConstraintViolation<Film>> violation = validator.validate(film);
        assertEquals(1, violation.size());
    }

    @Test
    void validateDuration() {
        Integer duration = 100;
        Film film = new Film(1L, "name", "description", LocalDate.now(), duration);
        Set<ConstraintViolation<Film>> violation = validator.validate(film);
        assertTrue(violation.isEmpty());
    }


}
