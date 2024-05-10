package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;

@Service
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private static final Integer DEFAULT_POPULAR_FILM_COUNT = 10;

    public FilmService(FilmRepository filmRepository, UserRepository userRepository) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
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

    public void addLike(long userId, long filmId) {
        userRepository.checkExistUser(userId);
        filmRepository.checkExistFilm(filmId);
        filmRepository.addLike(filmId, userId);
    }

    public void deleteLike(long userId, long filmId) {
        userRepository.checkExistUser(userId);
        filmRepository.checkExistFilm(filmId);
        filmRepository.deleteLike(filmId, userId);
    }

    public List<Film> getPopular(Integer count) {
        if (count == null) {
            count = DEFAULT_POPULAR_FILM_COUNT;
        }
        return filmRepository.getPopular(count);
    }
}
