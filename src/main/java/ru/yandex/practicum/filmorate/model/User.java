package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Validated
public class User {
    @NotNull(groups = Marker.OnUpdate.class)
    Long id;
    @Email
    String email;
    @Pattern(regexp = "\\S+")
    String login;
    String name;
    @Past
    LocalDate birthday;
}
