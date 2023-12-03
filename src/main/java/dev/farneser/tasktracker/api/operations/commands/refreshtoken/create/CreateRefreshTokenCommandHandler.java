package dev.farneser.tasktracker.api.operations.commands.refreshtoken.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.RefreshToken;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateRefreshTokenCommandHandler implements CommandHandler<CreateRefreshTokenCommand, Long> {
    private final RefreshTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long handle(CreateRefreshTokenCommand command) throws NotFoundException {
        var user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new UserNotFoundException(command.getUserId()));

        var tokens = tokenRepository.findByUserId(command.getUserId()).orElse(new ArrayList<>());

        if (tokens.isEmpty()) {
            var newToken = RefreshToken.builder()
                    .user(user)
                    .token(command.getToken())
                    .build();

            tokenRepository.save(newToken);

            return newToken.getId();
        } else {
            var token = tokens.get(0);
            token.setToken(command.getToken());

            tokenRepository.save(token);

            return token.getId();
        }
    }
}
