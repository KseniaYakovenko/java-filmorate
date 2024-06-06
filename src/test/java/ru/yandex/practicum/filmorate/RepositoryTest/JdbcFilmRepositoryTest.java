package ru.yandex.practicum.filmorate.RepositoryTest;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.JdbcFilmRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JdbcTest
@Import(JdbcFilmRepository.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcFilmRepositoryTest {

    private static final Long TEST_USER_1_ID = 1L;
    private static final Long TEST_USER_2_ID = 2L;

    private static final Long TEST_MPA_ID = 1L;
    private static final String TEST_MPA_NAME = "TEST_G";

    private static final Long TEST_GENRE_1_ID = 1L;
    private static final String TEST_GENRE_1_NAME = "TEST_Комедия";

    private static final Long TEST_GENRE_2_ID = 2L;
    private static final String TEST_GENRE_2_NAME = "TEST_Драма";

    private static final Long TEST_1_FILM_ID = 1L;
    private static final String TEST_1_FILM_NAME = "TEST_1_FILM_NAME";
    private static final String TEST_1_FILM_DESCRIPTION = "TEST_1_FILM_DESCRIPTION";
    private static final LocalDate TEST_1_FILM_RELEASE_DATE = LocalDate.of(2001, 1, 1);
    private static final Integer TEST_1_FILM_DURATION = 111;

    private static final Long TEST_2_FILM_ID = 2L;
    private static final String TEST_2_FILM_NAME = "TEST_2_FILM_NAME";
    private static final String TEST_2_FILM_DESCRIPTION = "TEST_2_FILM_DESCRIPTION";
    private static final LocalDate TEST_2_FILM_RELEASE_DATE = LocalDate.of(2002, 2, 2);
    private static final Integer TEST_2_FILM_DURATION = 222;

    private static final Long TEST_3_FILM_ID = 3L;
    private static final String TEST_3_FILM_NAME = "TEST_3_FILM_NAME";
    private static final String TEST_3_FILM_DESCRIPTION = "TEST_3_FILM_DESCRIPTION";
    private static final LocalDate TEST_3_FILM_RELEASE_DATE = LocalDate.of(2003, 3, 3);
    private static final Integer TEST_3_FILM_DURATION = 333;

    private final FilmRepository filmRepository;

    @Test
    public void testFindFilmById() {
        Film testFilm = getTestFilm1();
        saveGenreAndMpa(testFilm);
        Film film = filmRepository.getFilmById(testFilm.getId());
        Assertions.assertThat(film)
                .usingRecursiveComparison()
                .isEqualTo(getTestFilm1());
    }

    @Test
    public void testGetAllFilms() {
        List<Film> testFilms = getTestFilmsList_1_2();
        saveGenreAndMpa(testFilms.get(0));
        saveGenreAndMpa(testFilms.get(1));
        List<Film> films = filmRepository.getAll();
        Assertions.assertThat(films)
                .usingRecursiveComparison()
                .isEqualTo(getTestFilmsList_1_2());
    }

    @Test
    public void testSaveAndGetFilmWithMpaAndGenres() {
        Film testFilm = getTestFilm3();
        Film film = filmRepository.save(testFilm);
        testFilm.setId(film.getId());
        saveGenreAndMpa(film);
        Film filmFromDb = filmRepository.getFilmById(film.getId());
        Assertions.assertThat(testFilm)
                .usingRecursiveComparison()
                .isEqualTo(filmFromDb);
    }

    private void saveGenreAndMpa(Film film) {
        Long filmId = film.getId();
        filmRepository.saveFilmMpa(filmId, film.getMpa().getId());
        Set<Long> genreIds = film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        filmRepository.saveFilmGenre(filmId, genreIds);
    }

    @Test
    public void testAddAndDeleteLikeAndGetPopularFilm() {
        filmRepository.save(getTestFilm1());
        Film film2 = filmRepository.save(getTestFilm2());
        Film film3 = filmRepository.save(getTestFilm3());
        saveGenreAndMpa(film3);
        filmRepository.addLike(film3.getId(), TEST_USER_1_ID);
        filmRepository.addLike(film3.getId(), TEST_USER_2_ID);

        saveGenreAndMpa(film2);
        filmRepository.addLike(film2.getId(), TEST_USER_1_ID);
        Film popFilm = filmRepository.getPopular(10).get(0);
        System.out.println(filmRepository.getPopular(10));
        Assertions.assertThat(film3)
                .usingRecursiveComparison()
                .isEqualTo(popFilm);

        filmRepository.deleteLike(film3.getId(), TEST_USER_1_ID);
        filmRepository.deleteLike(film3.getId(), TEST_USER_2_ID);

        popFilm = filmRepository.getPopular(10).get(0);

        Assertions.assertThat(film2)
                .usingRecursiveComparison()
                .isEqualTo(popFilm);
    }

    static Mpa getTestMpa() {
        Mpa mpa = new Mpa();
        mpa.setId(TEST_MPA_ID);
        mpa.setName(TEST_MPA_NAME);
        return mpa;
    }

    static List<Genre> getTestGenres() {
        Genre genre1 = new Genre();
        genre1.setId(TEST_GENRE_1_ID);
        genre1.setName(TEST_GENRE_1_NAME);
        Genre genre2 = new Genre();
        genre2.setId(TEST_GENRE_2_ID);
        genre2.setName(TEST_GENRE_2_NAME);
        return List.of(genre1, genre2);
    }

    static Film getTestFilm1() {
        Film film = new Film();
        film.setId(TEST_1_FILM_ID);
        film.setName(TEST_1_FILM_NAME);
        film.setDescription(TEST_1_FILM_DESCRIPTION);
        film.setReleaseDate(TEST_1_FILM_RELEASE_DATE);
        film.setDuration(TEST_1_FILM_DURATION);
        film.setGenres(getTestGenres());
        film.setMpa(getTestMpa());
        return film;
    }

    static Film getTestFilm2() {
        Film film = new Film();
        film.setId(TEST_2_FILM_ID);
        film.setName(TEST_2_FILM_NAME);
        film.setDescription(TEST_2_FILM_DESCRIPTION);
        film.setReleaseDate(TEST_2_FILM_RELEASE_DATE);
        film.setDuration(TEST_2_FILM_DURATION);
        film.setGenres(getTestGenres());
        film.setMpa(getTestMpa());
        return film;
    }

    static Film getTestFilm3() {
        Film film = new Film();
        film.setId(TEST_3_FILM_ID);
        film.setName(TEST_3_FILM_NAME);
        film.setDescription(TEST_3_FILM_DESCRIPTION);
        film.setReleaseDate(TEST_3_FILM_RELEASE_DATE);
        film.setDuration(TEST_3_FILM_DURATION);
        film.setGenres(getTestGenres());
        film.setMpa(getTestMpa());
        return film;
    }

    static List<Film> getTestFilmsList_1_2() {
        return List.of(getTestFilm1(), getTestFilm2());
    }
}