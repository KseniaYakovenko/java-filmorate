package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;

    private static Film makeFilm(ResultSet rs, Map<Long, String> allGenresMap) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getInt("DURATION"));
        long mpaId = rs.getLong("MPA");
        String mpaName = rs.getString("MPA_NAME");
        if (mpaId != 0L) {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getLong("MPA"));
            mpa.setName(mpaName);
            film.setMpa(mpa);
        }

        Object[] genreIdsArray = (Object[]) rs.getArray("GENRES").getArray();
        if (genreIdsArray[0] != null) {
            List<Genre> genres = new ArrayList<>();
            for (Object obj : genreIdsArray) {
                Genre genre = new Genre();
                genre.setId((Long) obj);
                genre.setName(allGenresMap.get((Long) obj));
                genres.add(genre);
            }
            film.setGenres(genres);
        }
        return film;
    }


    @Override
    public List<Film> getAll() {
        Map<Long, String> allGenresMap = getAllGenresMap();
        return jdbc.query("""
                SELECT F.*, FM.MPA_ID AS MPA, M.MPA AS MPA_NAME, ARRAY_AGG (FG.GENRE_ID) AS GENRES from FILMS F
                LEFT JOIN FILMS_MPA FM ON F.FILM_ID = FM.FILM_ID
                LEFT JOIN MPA M ON FM.MPA_ID = M.MPA_ID
                LEFT JOIN FILMS_GENRES FG ON F.FILM_ID = FG.FILM_ID
                GROUP BY F.FILM_ID;
                """, rs -> {
            List<Film> filmList = new ArrayList<>();
            while (rs.next()) {
                Film film = makeFilm(rs, allGenresMap);
                filmList.add(film);
            }
            return filmList;
        });
    }

    private Map<Long, String> getAllGenresMap() {
        List<Genre> allGenres = getAllGenres();
        return allGenres.stream().collect(Collectors.toMap(Genre::getId, Genre::getName));
    }

    @Override
    public Film save(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource filmParams = new MapSqlParameterSource();
        filmParams.addValue("name", film.getName());
        filmParams.addValue("description", film.getDescription());
        filmParams.addValue("releaseDate", film.getReleaseDate());
        filmParams.addValue("duration", film.getDuration());

        jdbc.update(
                "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION) VALUES(:name, :description, :releaseDate, :duration)",
                filmParams,
                keyHolder,
                new String[]{"FILM_ID"}
        );
        Long filmId = keyHolder.getKeyAs(Long.class);
        film.setId(filmId);
        return film;
    }

    @Override
    public void saveFilmGenre(long filmId, Set<Long> genreIds) {
        HashMap<String, ?>[] batchOfInputs = new HashMap[genreIds.size()];
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        int count = 0;
        for (Long genreId : genreIds) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("filmId", filmId);
            map.put("genreId", genreId);
            batchOfInputs[count++] = map;
        }
        jdbc.update("DELETE FROM FiLMS_GENRES WHERE FILM_ID = :filmId", params);
        jdbc.batchUpdate("INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES(:filmId, :genreId)", batchOfInputs);
    }

    @Override
    public void saveFilmMpa(long filmId, long mpaId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        params.addValue("mpaId", mpaId);
        jdbc.update(
                """
                        DELETE FROM FiLMS_MPA WHERE FILM_ID = :filmId;
                        INSERT INTO FILMS_MPA (FILM_ID, MPA_ID) VALUES(:filmId, :mpaId)
                        """,
                params
        );
    }

    @Override
    public Film update(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource filmParams = new MapSqlParameterSource();
        filmParams.addValue("id", film.getId());
        filmParams.addValue("name", film.getName());
        filmParams.addValue("description", film.getDescription());
        filmParams.addValue("releaseDate", film.getReleaseDate());
        filmParams.addValue("duration", film.getDuration());

        int row = jdbc.update(
                """
                             UPDATE FILMS
                             SET NAME = :name,
                                 DESCRIPTION = :description,
                                 RELEASE_DATE = :releaseDate,
                                 DURATION = :duration
                             WHERE FILM_ID = :id
                        """,
                filmParams,
                keyHolder
        );
        if (row != 1) throw new NotFoundException("Нет фильма с id = " + film.getId());

        return film;
    }


    @Override
    public void addLike(long filmId, long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("filmId", filmId);
        jdbc.update(
                "MERGE INTO USER_FILM_LIKES KEY (USER_ID, FILM_ID) VALUES(:userId, :filmId)", params);
    }


    @Override
    public void deleteLike(long filmId, long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("filmId", filmId);
        jdbc.update(
                "DELETE FROM USER_FILM_LIKES WHERE USER_ID =:userId AND FILM_ID = :filmId", params);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        Map<Long, String> allGenresMap = getAllGenresMap();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("count", count);
        return jdbc.query("""
                                SELECT F.*, COUNT(L.USER_ID) as RATE FROM (
                                    SELECT F.*, FM.MPA_ID AS MPA, M.MPA AS MPA_NAME, ARRAY_AGG (FG.GENRE_ID) AS GENRES from FILMS F
                                    LEFT JOIN FILMS_MPA FM ON F.FILM_ID = FM.FILM_ID
                                    LEFT JOIN MPA M ON FM.MPA_ID = M.MPA_ID
                                    LEFT JOIN FILMS_GENRES FG ON F.FILM_ID = FG.FILM_ID
                                    GROUP BY F.FILM_ID) as F
                                LEFT JOIN USER_FILM_LIKES L ON F.FILM_ID = L.FILM_ID
                                GROUP BY F.FILM_ID
                                ORDER BY RATE DESC
                                LIMIT :count;
                """, params, rs -> {
            List<Film> filmList = new ArrayList<>();
            while (rs.next()) {
                Film film = makeFilm(rs, allGenresMap);
                filmList.add(film);
            }
            return filmList;
        });
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbc.query("SELECT * FROM MPA", rs -> {
            List<Mpa> mpaList = new ArrayList<>();
            while (rs.next()) {
                Mpa mpa = new Mpa();
                mpa.setId(rs.getLong("MPA_ID"));
                mpa.setName(rs.getString("MPA"));
                mpaList.add(mpa);
            }
            return mpaList;
        });
    }

    @Override
    public Mpa getMpaById(Long mpaId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mpaId", mpaId);
        return jdbc.query(
                "SELECT * FROM MPA WHERE MPA_ID = :mpaId",
                params, rs -> {
                    if (!rs.next()) {
                        throw new NotFoundException("Нет MPA с id = " + mpaId);
                    }
                    Mpa mpa = new Mpa();
                    mpa.setId(rs.getLong("MPA_ID"));
                    mpa.setName(rs.getString("MPA"));
                    return mpa;
                });
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbc.query("SELECT * FROM GENRES", rs -> {
            List<Genre> genreList = new ArrayList<>();
            while (rs.next()) {
                Genre genre = new Genre();
                genre.setId(rs.getLong("GENRE_ID"));
                genre.setName(rs.getString("GENRE"));
                genreList.add(genre);
            }
            return genreList;
        });
    }

    @Override
    public Genre getGenresById(Long genreId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genreId", genreId);
        return jdbc.query(
                "SELECT * FROM GENRES WHERE GENRE_ID = :genreId",
                params, rs -> {
                    if (!rs.next()) {
                        throw new NotFoundException("Нет genre с id = " + genreId);
                    }
                    Genre genre = new Genre();
                    genre.setId(rs.getLong("GENRE_ID"));
                    genre.setName(rs.getString("GENRE"));
                    return genre;
                });
    }

    @Override
    public Film getFilmById(long filmId) {
        Map<Long, String> allGenresMap = getAllGenresMap();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        return jdbc.query("""
                SELECT F.*, FM.MPA_ID AS MPA, M.MPA AS MPA_NAME, ARRAY_AGG (FG.GENRE_ID) AS GENRES from FILMS F
                LEFT JOIN FILMS_MPA FM ON F.FILM_ID = FM.FILM_ID
                LEFT JOIN MPA M ON FM.MPA_ID = M.MPA_ID
                LEFT JOIN FILMS_GENRES FG ON F.FILM_ID = FG.FILM_ID
                WHERE F.FILM_ID = :filmId
                GROUP BY F.FILM_ID
                """, params, rs -> {
            if (!rs.next()) {
                throw new NotFoundException("Нет фильма с id = " + filmId);
            }
            return makeFilm(rs, allGenresMap);
        });
    }

    @Override
    public void checkExistFilm(Long filmId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        Boolean exist = jdbc.query(
                "SELECT * FROM FILMS WHERE FILM_ID = :filmId",
                params, ResultSet::next);

        if (Boolean.FALSE.equals(exist)) {
            throw new NotFoundException("Нет фильма с id = " + filmId);
        }
    }

    @Override
    public boolean checkExistGenres(Set<Long> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
        Boolean exist = jdbc.query(
                "SELECT * FROM GENRES WHERE GENRE_ID in (:ids)",
                params, ResultSet::next);
        return Boolean.TRUE.equals(exist);
    }

    @Override
    public boolean checkExistMpa(Mpa mpa) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mpaId", mpa.getId());
        Boolean exist = jdbc.query(
                "SELECT * FROM MPA WHERE MPA_ID = :mpaId",
                params, ResultSet::next);
        return Boolean.TRUE.equals(exist);
    }
}
