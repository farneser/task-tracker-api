package dev.farneser.tasktracker.api.operations.commands.user.register;

import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.models.permissions.Role;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterUserCommandHandler implements CommandHandler<RegisterUserCommand, Long> {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public Long handle(RegisterUserCommand command) throws ValidationException {
        log.debug("Registering user: {}", command);

//        Set<ConstraintViolation<RegisterUserCommand>> violations = validator.validate(command);
//
//        if (!violations.isEmpty()) {
//            throw new ValidationException("Validation error: " + violations.iterator().next().getMessage());
//        }

        User user = User
                .builder()
                .username(command.getUsername())
                .email(command.getEmail())
                .password(passwordEncoder.encode(command.getPassword()))
                .registerDate(new Date(System.currentTimeMillis()))
                .isEnabled(false)
                .isLocked(false)
                .role(Role.USER)
                .isSubscribed(command.isSubscribed())
                .build();

        log.debug("User created: {}", user);

        user = userRepository.save(user);

        log.debug("User saved: {}", user);

        return user.getId();
    }
}
