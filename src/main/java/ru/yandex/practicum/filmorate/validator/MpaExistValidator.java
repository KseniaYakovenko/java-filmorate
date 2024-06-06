
package ru.yandex.practicum.filmorate.validator;

        import jakarta.validation.ConstraintValidator;
        import jakarta.validation.ConstraintValidatorContext;
        import ru.yandex.practicum.filmorate.model.Mpa;
        import ru.yandex.practicum.filmorate.service.MpaService;

public class MpaExistValidator implements ConstraintValidator<MpaConstraint, Mpa> {
    private final MpaService mpaService;

    public MpaExistValidator(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @Override
    public boolean isValid(Mpa mpa, ConstraintValidatorContext constraintValidatorContext) {
        if (mpa != null) {
            return mpaService.checkExistMpa(mpa);
        } else return true;
    }
}
