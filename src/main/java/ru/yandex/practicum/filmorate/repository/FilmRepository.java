package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

@Repository
public class FilmRepository {
    private final HashMap<Long, Film> films = new HashMap<>();
    private Long generatorId = 0L;

    public Long generateId() {
        return ++generatorId;
    }

    public List<Film> getAll() {
        return films.values().stream().toList();
    }

    public Film save(Film film) {
        Long filmId = generateId();
        film.setId(filmId);
        films.put(filmId, film);
        return films.get(filmId);
    }

    public Film update(Film film) {
        Long id = film.getId();
        Film oldFilm = films.get(id);
        if (oldFilm == null) {
            throw new NotFoundException("Нет фильма с id = " + id);
        }
        films.put(id, film);
        return films.get(id);
    }
}