package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.util.List;

@Slf4j
@RestController
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
}
