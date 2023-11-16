package dev.farneser.tasktracker.api.operations.commands.refreshtoken.deletebyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyuserid.GetRefreshTokenByUserIdQuery;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteRefreshTokenByUserIdCommandHandler implements CommandHandler<DeleteRefreshTokenByUserIdCommand, Void> {
    private final RefreshTokenRepository refreshTokenRepository;
    private final Mediator mediator;

    @Override
    public Void handle(DeleteRefreshTokenByUserIdCommand command) throws NotFoundException {
        var token = mediator.send(new GetRefreshTokenByUserIdQuery(command.getUserId()));

        refreshTokenRepository.delete(token);

        return null;
    }
}
