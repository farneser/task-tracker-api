package dev.farneser.tasktracker.api.operations.commands.user.register;

import dev.farneser.tasktracker.api.web.dto.auth.RegisterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        var user = (RegisterDto) obj;

        log.debug("Validating password matches for user: {}", user);

        return user.getPassword().equals(user.getConfirmPassword());
    }
}