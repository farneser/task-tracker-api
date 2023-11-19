package dev.farneser.tasktracker.api.operations.commands.user.register;

import dev.farneser.tasktracker.api.web.dto.auth.RegisterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        var user = (RegisterDto) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}