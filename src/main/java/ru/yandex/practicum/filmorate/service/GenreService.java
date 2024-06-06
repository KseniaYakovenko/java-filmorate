package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {
    private final FilmRepository filmRepository;

    public GenreService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<Genre> getAll() {
        return filmRepository.getAllGenres();
    }

    public Genre getGenresById(Long genreId) {
        return filmRepository.getGenresById(genreId);
    }

    public boolean checkExistGenres(List<Genre> genres) {
        return filmRepository.checkExistGenres(genres.stream().map(Genre::getId).collect(Collectors.toSet()));
    }
}
