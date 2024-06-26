package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.validator.Marker;
import ru.yandex.practicum.filmorate.validator.ReleaseDateConstraint;

import java.time.LocalDate;

@Data
@AllArgsConstructor
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
}
