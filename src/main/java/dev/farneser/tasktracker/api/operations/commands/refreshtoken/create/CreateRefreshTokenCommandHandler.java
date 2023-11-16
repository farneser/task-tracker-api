package dev.farneser.tasktracker.api.operations.commands.refreshtoken.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.models.RefreshToken;
import dev.farneser.tasktracker.api.operations.queries.user.getbyid.GetUserByIdQuery;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateRefreshTokenCommandHandler implements CommandHandler<CreateRefreshTokenCommand, Long> {
    private final RefreshTokenRepository refreshTokenRepository;
    private final Mediator mediator;

    @Override
    public Long handle(CreateRefreshTokenCommand command) throws NotFoundException {
        var user = mediator.send(new GetUserByIdQuery(command.getUserId()));

        var token = RefreshToken.builder().token(command.getToken()).user(user).build();

        refreshTokenRepository.save(token);

        return token.getId();
    }
}
