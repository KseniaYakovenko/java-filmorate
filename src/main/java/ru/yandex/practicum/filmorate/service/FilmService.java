package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.List;

@Service
public class FilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Film save(Film film) {
        return filmRepository.save(film);
    }

    public List<Film> getAll() {
        return filmRepository.getAll();
    }

    public Film update(Film film) {
        return filmRepository.update(film);
    }
}
