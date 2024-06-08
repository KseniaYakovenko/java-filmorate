package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

public class GenreExistValidator implements ConstraintValidator<GenreConstraint, List<Genre>> {
    private final GenreService genreService;

    public GenreExistValidator(GenreService genreService) {
        this.genreService = genreService;
    }

    @Override
    public boolean isValid(List<Genre> genres, ConstraintValidatorContext constraintValidatorContext) {
        return genres == null || genreService.checkExistGenres(genres);
    }
}
