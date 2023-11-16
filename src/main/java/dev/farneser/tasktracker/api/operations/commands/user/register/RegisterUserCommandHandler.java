package dev.farneser.tasktracker.api.operations.commands.user.register;

import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class RegisterUserCommandHandler implements CommandHandler<RegisterUserCommand, Long> {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public Long handle(RegisterUserCommand command) {

        var user = User
                .builder()
                .email(command.getEmail())
                .password(passwordEncoder.encode(command.getPassword()))
                .registerDate(new Date(System.currentTimeMillis()))
                .build();

        user = userRepository.save(user);

        return user.getId();
    }
}
