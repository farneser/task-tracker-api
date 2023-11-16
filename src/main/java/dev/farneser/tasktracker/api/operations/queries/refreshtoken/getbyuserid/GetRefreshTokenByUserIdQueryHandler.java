package dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.RefreshTokenNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.RefreshToken;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetRefreshTokenByUserIdQueryHandler implements QueryHandler<GetRefreshTokenByUserIdQuery, RefreshToken> {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken handle(GetRefreshTokenByUserIdQuery query) throws NotFoundException {
        return refreshTokenRepository.findByUserId(query.getId()).orElseThrow(() -> new RefreshTokenNotFoundException(query.getId()));
    }
}
