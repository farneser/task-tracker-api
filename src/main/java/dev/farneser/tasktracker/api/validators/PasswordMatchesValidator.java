package dev.farneser.tasktracker.api.validators;

import dev.farneser.tasktracker.api.annotations.PasswordMatches;
import dev.farneser.tasktracker.api.web.dto.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        var user = (RegisterRequest) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}