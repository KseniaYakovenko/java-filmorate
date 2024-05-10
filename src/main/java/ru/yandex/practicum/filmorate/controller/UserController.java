package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@ResponseBody
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    List<User> getAllUser() {
        return userService.getAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    User saveUser(@RequestBody @Valid User user) {
        log.info("Create user: {} - Started", user);
        User savedUser = userService.save(user);
        log.info("Create user: {} - Finished", savedUser);
        return savedUser;
    }

    @PutMapping()
    User updateUser(@RequestBody @Validated(Marker.OnUpdate.class) User user) {
        log.info("Update user: {} - Started", user);
        User updatedUser = userService.update(user);
        log.info("Update user: {} - Finished", updatedUser);
        return updatedUser;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.info("addFriend with id: {} for user: {} - Started", friendId, userId);
        userService.addFriend(userId, friendId);
        log.info("addFriend with id: {} for user: {} - Finished", friendId, userId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.info("deleteFriend with id: {} for user: {} - Started", friendId, userId);
        userService.deleteFriend(userId, friendId);
        log.info("deleteFriend with id: {} for user: {} - Finished", friendId, userId);
    }

    @GetMapping("/{userId}/friends")
    Set<User> getFriends(@PathVariable long userId) {
        log.info("getFriends for user: {} - Started",userId);
        log.info("getFriends for user: {} - Finished", userId);
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    Set<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherUserId) {
        log.info("getCommonFriends for users: {}, {} - Started", userId, otherUserId);
        log.info("getCommonFriends for users: {}, {} - Finished", userId, otherUserId);
        return userService.getCommonFriends(userId, otherUserId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(final NotFoundException e){
        log.info("Error", e);
        ErrorResponse errorResponse = new ru.yandex.practicum.filmorate.controller.ErrorResponse(e.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        errorResponse.setStacktrace(pw.toString());
        return errorResponse;
    }
}
