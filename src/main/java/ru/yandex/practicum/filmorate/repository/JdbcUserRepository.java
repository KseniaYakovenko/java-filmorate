package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<User> getAll() {

        return jdbc.query("SELECT * FROM USERS", rs -> {
            List<User> userList = new ArrayList<>();
            while (rs.next()) {
                User user = makeUser(rs);
                userList.add(user);
            }
            return userList;
        });
    }

    private static User makeUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("USER_ID"));
        user.setEmail(rs.getString("E_MAIL"));
        user.setLogin(rs.getString("LOGIN"));
        user.setName(rs.getString("USER_NAME"));
        user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
        return user;
    }

    @Override
    public User save(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("userName", user.getName());
        params.addValue("birthday", user.getBirthday());
        jdbc.update(
                "INSERT INTO USERS(E_MAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES(:email, :login, :userName, :birthday)",
                params,
                keyHolder,
                new String[]{"USER_ID"}
        );
        Long id = keyHolder.getKeyAs(Long.class);
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", user.getId());
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("userName", user.getName());
        params.addValue("birthday", user.getBirthday());
        int row = jdbc.update(
                """
                             UPDATE USERS
                             SET E_MAIL = :email,
                                 LOGIN = :login,
                                 USER_NAME = :userName,
                                 BIRTHDAY = :birthday
                             WHERE USER_ID = :id
                        """,
                params,
                keyHolder
        );
        if (row != 1) throw new NotFoundException("Нет пользователя с id = " + user.getId());
        return user;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("friendId", friendId);
        jdbc.update(
                "MERGE INTO FRIENDS KEY (USER_ID, FRIEND_ID) VALUES(:userId, :friendId)", params);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("friendId", friendId);
        jdbc.update(
                "DELETE FROM FRIENDS WHERE USER_ID =:userId AND FRIEND_ID = :friendId", params);
    }

    @Override
    public Set<User> getFriends(long userId) {
        checkExistUser(userId);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        return jdbc.query("""
                SELECT * FROM USERS
                JOIN FRIENDS F on USERS.USER_ID = F.FRIEND_ID
                WHERE F.USER_ID=:userId
                """, params, rs -> {
            Set<User> userList = new HashSet<>();
            while (rs.next()) {
                User user = makeUser(rs);
                userList.add(user);
            }
            return userList;
        });
    }

    @Override
    public Set<User> getCommonFriends(long userId, long otherUserId) {
        checkExistUser(userId);
        checkExistUser(otherUserId);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("otherUserId", otherUserId);
        return jdbc.query("""
                SELECT U.* FROM USERS U
                JOIN FRIENDS F_1 on U.USER_ID = F_1.FRIEND_ID
                JOIN FRIENDS F_2 on F_1.FRIEND_ID=F_2.FRIEND_ID
                WHERE F_1.USER_ID = :userId and F_2.USER_ID = :otherUserId;
                """, params, rs -> {
            Set<User> userList = new HashSet<>();
            while (rs.next()) {
                User user = makeUser(rs);
                userList.add(user);
            }
            return userList;
        });
    }

    @Override
    public void checkExistUser(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", id);
        Boolean exist = jdbc.query(
                "SELECT * FROM USERS WHERE USER_ID = :userId",
                params, ResultSet::next);

        if (Boolean.FALSE.equals(exist)) {
            throw new NotFoundException("Нет пользователя с id = " + id);
        }
    }
}
