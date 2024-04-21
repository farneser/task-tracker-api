package dev.farneser.tasktracker.api.operations.commands.user.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatchUserCommandHandler implements CommandHandler<PatchUserCommand, Void> {
    private final UserRepository userRepository;

    @Override
    public Void handle(PatchUserCommand command) throws NotFoundException {
        User user = userRepository.findById(command.getUserId()).orElseThrow(() -> new UserNotFoundException(command.getUserId()));

        log.debug("User found: {}", user);

        if (command.getIsSubscribed() != null) {
            log.debug("User subscribed: {}", command.getIsSubscribed());

            user.setSubscribed(command.getIsSubscribed());
        }

        userRepository.save(user);

        log.debug("User saved: {}", user);

        return null;
    }
}
