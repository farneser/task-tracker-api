package dev.farneser.tasktracker.api.operations.commands.user.activate;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivateUserCommandHandler implements CommandHandler<ActivateUserCommand, Long> {
    private final UserRepository userRepository;

    @Override
    public Long handle(ActivateUserCommand command) throws NotFoundException {
        var user = userRepository.findByEmail(command.getEmail()).orElseThrow(() -> new UserNotFoundException(command.getEmail()));

        log.debug("User found: {}", user);

        user.setEnabled(true);

        log.debug("User enabled: {}", user);

        userRepository.save(user);

        log.debug("User saved: {}", user);

        return user.getId();
    }
}
