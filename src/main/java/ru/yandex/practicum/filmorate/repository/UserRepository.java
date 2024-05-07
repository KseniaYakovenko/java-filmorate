package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;

@Repository
public class UserRepository {
    private final HashMap<Long, User> users = new HashMap<>();
    private Long generatorId = 0L;

    public Long generateId() {
        return ++generatorId;
    }

    public List<User> getAll() {
        return users.values().stream().toList();
    }

    public User save(User user) {
        Long userId = generateId();
        user.setId(userId);
        users.put(userId, user);
        return users.get(userId);
    }

    public User update(User user) {
        Long id = user.getId();
        User oldUser = users.get(id);
        if (oldUser == null) {
            throw new NotFoundException("Нет пользователя с id = " + id);
        }
        users.put(id, user);
        return users.get(id);
    }
}