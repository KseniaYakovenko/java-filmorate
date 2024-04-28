package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void validateEmptyLogin() {
        String emptyLogin = "";
        User user = new User(1L, "e@mail", emptyLogin, "name", LocalDate.now().minusDays(1L));
        Set<ConstraintViolation<User>> violation = validator.validate(user);
        assertEquals(1, violation.size());
    }

    @Test
    void validateFailLogin() {
        String loginWithSpace = "login login";
        User user = new User(1L, "e@mail", loginWithSpace, "name", LocalDate.now().minusDays(1L));
        Set<ConstraintViolation<User>> violation = validator.validate(user);
        assertEquals(1, violation.size());
    }

    @Test
    void validateNotEmptyLogin() {
        String notEmptyLogin = "login";
        User user = new User(1L, "e@mail", notEmptyLogin, "name", LocalDate.now().minusDays(1L));
        Set<ConstraintViolation<User>> violation = validator.validate(user);
        assertTrue(violation.isEmpty());
    }

    @Test
    void validateEmail() {
        String email = "email@yandex.ru";
        User user = new User(1L, email, "notEmptyLogin", "name", LocalDate.now().minusDays(1L));
        Set<ConstraintViolation<User>> violation = validator.validate(user);
        assertTrue(violation.isEmpty());
    }

    @Test
    void validateFailEmail() {
        String email = "yandex.ru";
        User user = new User(1L, email, "notEmptyLogin", "name", LocalDate.now().minusDays(1L));
        Set<ConstraintViolation<User>> violation = validator.validate(user);
        assertEquals(1, violation.size());
    }


}
