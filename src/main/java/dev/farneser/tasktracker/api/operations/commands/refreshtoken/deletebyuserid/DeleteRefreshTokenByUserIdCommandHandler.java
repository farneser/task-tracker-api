package dev.farneser.tasktracker.api.operations.commands.refreshtoken.deletebyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyuserid.GetRefreshTokenByUserIdQuery;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteRefreshTokenByUserIdCommandHandler implements CommandHandler<DeleteRefreshTokenByUserIdCommand, Void> {
    private final RefreshTokenRepository refreshTokenRepository;
    private final Mediator mediator;

    @Override
    public Void handle(DeleteRefreshTokenByUserIdCommand command) {
        try {
            var token = mediator.send(new GetRefreshTokenByUserIdQuery(command.getUserId()));
            log.debug("Refresh token found for user with id: {}", command.getUserId());

            refreshTokenRepository.delete(token);
            log.debug("Refresh token deleted for user with id: {}", command.getUserId());

        } catch (NotFoundException e) {
            log.debug("Refresh token not found for user with id: {}", command.getUserId());
        }

        return null;
    }
}
