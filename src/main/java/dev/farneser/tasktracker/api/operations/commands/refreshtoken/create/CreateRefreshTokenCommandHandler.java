package dev.farneser.tasktracker.api.operations.commands.refreshtoken.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.RefreshToken;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateRefreshTokenCommandHandler implements CommandHandler<CreateRefreshTokenCommand, Long> {
    private final RefreshTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long handle(CreateRefreshTokenCommand command) throws NotFoundException {
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new UserNotFoundException(command.getUserId()));

        log.debug("User found: {}", user);

        List<RefreshToken> tokens = tokenRepository.findByUserId(command.getUserId()).orElse(new ArrayList<>());

        if (tokens.isEmpty()) {
            log.debug("No tokens found for user {}", user.getId());

            RefreshToken newToken = RefreshToken.builder()
                    .user(user)
                    .token(command.getToken())
                    .build();
            log.debug("Token created for user {}", user.getId());

            tokenRepository.save(newToken);

            log.debug("Token saved for user {}", user.getId());

            return newToken.getId();
        } else {
            log.debug("Tokens found for user {}", user.getId());

            RefreshToken token = tokens.get(0);
            token.setToken(command.getToken());

            log.debug("Token updated for user {}", user.getId());

            tokenRepository.save(token);

            log.debug("Token saved for user {}", user.getId());

            return token.getId();
        }
    }
}
