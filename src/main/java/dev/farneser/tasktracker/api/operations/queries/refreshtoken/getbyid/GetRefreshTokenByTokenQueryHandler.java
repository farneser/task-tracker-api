package dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.RefreshTokenNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.RefreshToken;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetRefreshTokenByTokenQueryHandler implements QueryHandler<GetRefreshTokenByTokenQuery, RefreshToken> {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken handle(GetRefreshTokenByTokenQuery query) throws NotFoundException {
        return refreshTokenRepository.findByToken(query.getToken()).orElseThrow(() -> new RefreshTokenNotFoundException(query.getToken()));
    }
}
