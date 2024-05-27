package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        checkName(user);
        return userRepository.save(user);
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public User update(User user) {
        checkName(user);
        return userRepository.update(user);
    }

    public void addFriend(long userId, long friendId) {
        userRepository.checkExistUser(userId);
        userRepository.checkExistUser(friendId);
        if (userId != friendId) {
            userRepository.addFriend(userId, friendId);
        }
    }

    public void deleteFriend(long userId, long friendId) {
        userRepository.checkExistUser(userId);
        userRepository.checkExistUser(friendId);
        userRepository.deleteFriend(userId, friendId);
    }

    public Set<User> getFriends(long userId) {
        userRepository.checkExistUser(userId);
        return userRepository.getFriends(userId);
    }

    public Set<User> getCommonFriends(long userId, long otherUserId) {
        userRepository.checkExistUser(userId);
        userRepository.checkExistUser(otherUserId);
        return userRepository.getCommonFriends(userId, otherUserId);
    }
}
