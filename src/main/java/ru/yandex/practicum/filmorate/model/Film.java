package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.validator.GenreConstraint;
import ru.yandex.practicum.filmorate.validator.Marker;
import ru.yandex.practicum.filmorate.validator.MpaConstraint;
import ru.yandex.practicum.filmorate.validator.ReleaseDateConstraint;

import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
@Validated
public class Film {
    @NotNull(groups = Marker.OnUpdate.class)
    Long id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @ReleaseDateConstraint
    LocalDate releaseDate;
    @Positive
    Integer duration;
    @GenreConstraint
    List<Genre> genres;
    @MpaConstraint
    Mpa mpa;
}
