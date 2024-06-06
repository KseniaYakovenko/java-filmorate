package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Set;

public interface FilmRepository {
    List<Film> getAll();

    Film save(Film film);

    void saveFilmGenre(long filmId, Set<Long> genreIds);

    void saveFilmMpa(long filmId, long mpaId);

    Film update(Film film);

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Film> getPopular(Integer count);

    void checkExistFilm(Long filmId);

    boolean checkExistGenres(Set<Long> ids);

    boolean checkExistMpa(Mpa mpa);

    List<Mpa> getAllMpa();

    Mpa getMpaById(Long mpaId);

    List<Genre> getAllGenres();

    Genre getGenresById(Long genreId);

    Film getFilmById(long filmId);
}
