package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.List;

@Service
public class MpaService {
    private final FilmRepository filmRepository;

    public MpaService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<Mpa> getAll() {
        return filmRepository.getAllMpa();
    }

    public Mpa getMpaById(Long mpaId) {
        return filmRepository.getMpaById(mpaId);
    }

    public boolean checkExistMpa(Mpa mpa) {
        return filmRepository.checkExistMpa(mpa);
    }
}
