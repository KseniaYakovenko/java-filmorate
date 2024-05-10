package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;

@Repository
public class InMemoryFilmRepository implements FilmRepository {
    private final HashMap<Long, Film> films = new HashMap<>();
    private final HashMap<Long, Set<Long>> filmsLikesByUserIds = new HashMap<>();
    private Long generatorId = 0L;

    public Long generateId() {
        return ++generatorId;
    }

    @Override
    public List<Film> getAll() {
        return films.values().stream().toList();
    }

    @Override
    public Film save(Film film) {
        Long filmId = generateId();
        film.setId(filmId);
        films.put(filmId, film);
        return films.get(filmId);
    }

    @Override
    public Film update(Film film) {
        Long id = film.getId();
        Film oldFilm = films.get(id);
        if (oldFilm == null) {
            throw new NotFoundException("Нет фильма с id = " + id);
        }
        films.put(id, film);
        return films.get(id);
    }

    @Override
    public void addLike(long filmId, long userId) {
        Set<Long> filmLikeIds = filmsLikesByUserIds.computeIfAbsent(filmId, id -> new HashSet<>());
        filmLikeIds.add(userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        Set<Long> filmLikeIds = filmsLikesByUserIds.get(filmId);
        if (filmLikeIds == null) throw new NotFoundException("Нет фильма с id = " + userId);
        if (!filmLikeIds.contains(userId)) {
            throw new NotFoundException("Нет лайка от пользователя с id = " + userId);
        }
        filmLikeIds.remove(userId);
    }

    @Override
    public List<Film> getPopular(Integer count) {

        LinkedList<Film> popFilms = new LinkedList<>();
        filmsLikesByUserIds.entrySet()
                .stream()
                .sorted((v1, v2) -> v2.getValue().size() - v1.getValue().size())
                .limit(count)
                .forEach(item -> popFilms.add(films.get(item.getKey())));
        return popFilms;
    }

    public void checkExistFilm(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Нет фильма с id = " + id);
        }
    }
}