package dev.farneser.tasktracker.api.operations.commands.refreshtoken.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.RefreshToken;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateRefreshTokenCommandHandler implements CommandHandler<CreateRefreshTokenCommand, Long> {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public Long handle(CreateRefreshTokenCommand command) throws NotFoundException {
        var user = userRepository.findById(command.getUserId()).orElseThrow(() -> new UserNotFoundException(command.getUserId()));

        var token = RefreshToken.builder().token(command.getToken()).user(user).build();

        refreshTokenRepository.save(token);

        return token.getId();
    }
}
