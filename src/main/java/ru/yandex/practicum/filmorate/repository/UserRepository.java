package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserRepository {
    List<User> getAll();

    User save(User user);

    User update(User user);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    Set<User> getFriends(long userId);

    Set<User> getCommonFriends(long userId, long otherUserId);

    void checkExistUser(Long id);
}
