package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Film savedFilm = filmRepository.save(film);
        List<Genre> genres = film.getGenres();
        Mpa mpa = film.getMpa();
        Long filmId = savedFilm.getId();
        if (genres != null && !genres.isEmpty()) {
            Set<Long> genreIds = genres.stream().map(Genre::getId).collect(Collectors.toSet());
            filmRepository.saveFilmGenre(filmId, genreIds);
        }
        if (mpa != null) {
            filmRepository.saveFilmMpa(filmId, mpa.getId());
        }
        return savedFilm;
    }

    public List<Film> getAll() {
        return filmRepository.getAll();
    }

    public Film update(Film film) {
        Film updatedFilm = filmRepository.update(film);
        List<Genre> genres = film.getGenres();
        Long mpaId = film.getMpa().getId();
        Long filmId = updatedFilm.getId();
        if (genres != null && !genres.isEmpty()) {
            Set<Long> genreIds = genres.stream().map(Genre::getId).collect(Collectors.toSet());
            filmRepository.saveFilmGenre(filmId, genreIds);
            filmRepository.saveFilmMpa(filmId, mpaId);
        }

        return updatedFilm;
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

    public Film getFilmByID(long filmId) {
        return filmRepository.getFilmById(filmId);
    }
}
