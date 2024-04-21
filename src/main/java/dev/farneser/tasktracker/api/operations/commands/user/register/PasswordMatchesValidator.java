package dev.farneser.tasktracker.api.operations.commands.user.register;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterUserCommand> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(RegisterUserCommand command, ConstraintValidatorContext context) {
        log.debug("Validating password matches for user: {}", command.getEmail());

        return command.getPassword().equals(command.getConfirmPassword());
    }
}