package dev.farneser.tasktracker.api.operations.commands.user.register;

import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.User;
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
    public Long handle(RegisterUserCommand command) {
        log.debug("Registering user: {}", command);

        User user = User
                .builder()
                .email(command.getEmail())
                .password(passwordEncoder.encode(command.getPassword()))
                .registerDate(new Date(System.currentTimeMillis()))
                .build();
        log.debug("User created: {}", user);

        user = userRepository.save(user);

        log.debug("User saved: {}", user);

        return user.getId();
    }
}
