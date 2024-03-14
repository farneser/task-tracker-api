package dev.farneser.tasktracker.api.operations.commands.refreshtoken.delete;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.models.RefreshToken;
import dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyid.GetRefreshTokenByTokenQuery;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteRefreshTokenCommandHandler implements CommandHandler<DeleteRefreshTokenCommand, Void> {
    private final RefreshTokenRepository refreshTokenRepository;
    private final Mediator mediator;

    @Override
    public Void handle(DeleteRefreshTokenCommand command) throws NotFoundException {
        RefreshToken token = mediator.send(new GetRefreshTokenByTokenQuery(command.getToken()));

        refreshTokenRepository.delete(token);

        log.debug("Refresh token deleted: {}", command.getToken());

        return null;
    }
}
