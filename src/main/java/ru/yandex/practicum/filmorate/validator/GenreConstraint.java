
package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = GenreExistValidator.class)
@Documented
public @interface GenreConstraint {

    String message() default "{GenresList.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}