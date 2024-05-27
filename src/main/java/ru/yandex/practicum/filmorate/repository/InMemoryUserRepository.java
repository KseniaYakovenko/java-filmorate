package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final HashMap<Long, User> users = new HashMap<>();
    private final HashMap<Long, Set<Long>> userFriendIds = new HashMap<>();
    private Long generatorId = 0L;

    public Long generateId() {
        return ++generatorId;
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public User save(User user) {
        Long userId = generateId();
        user.setId(userId);
        users.put(userId, user);
        return users.get(userId);
    }

    @Override
    public User update(User user) {
        Long id = user.getId();
        User oldUser = users.get(id);
        if (oldUser == null) {
            throw new NotFoundException("Нет пользователя с id = " + id);
        }
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        Set<Long> uFriendIds = userFriendIds.computeIfAbsent(userId, id -> new HashSet<>());
        uFriendIds.add(friendId);
        Set<Long> fFriendIds = userFriendIds.computeIfAbsent(friendId, id -> new HashSet<>());
        fFriendIds.add(userId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        Set<Long> uFriendIds = userFriendIds.get(userId);
        if (uFriendIds != null) {
            uFriendIds.remove(friendId);
        }
        Set<Long> fFriendIds = userFriendIds.get(friendId);
        if (fFriendIds != null) {
            fFriendIds.remove(userId);
        }
    }

    @Override
    public Set<User> getFriends(long userId) {
        Set<Long> uFriendIds = userFriendIds.get(userId);
        Set<User> uFriends = new HashSet<>();
        if (uFriendIds != null) {
            uFriendIds.forEach(id -> uFriends.add(users.get(id)));
        }
        return uFriends;
    }

    @Override
    public Set<User> getCommonFriends(long userId, long otherUserId) {
        Set<Long> firstUserFriendIds = userFriendIds.get(userId);
        Set<Long> secondUserFriendIds = userFriendIds.get(otherUserId);
        if (firstUserFriendIds == null || secondUserFriendIds == null) {
            return Collections.emptySet();
        }
        firstUserFriendIds.retainAll(secondUserFriendIds);
        Set<User> commonFriends = new HashSet<>();
        firstUserFriendIds.forEach(id -> commonFriends.add(users.get(id)));
        return commonFriends;
    }

    public void checkExistUser(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Нет пользователя с id = " + id);
        }
    }
}