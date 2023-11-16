package dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbytoken;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.RefreshTokenNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.RefreshToken;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GetRefreshTokenByIdQueryHandler implements QueryHandler<GetRefreshTokenByIdQuery, RefreshToken> {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken handle(GetRefreshTokenByIdQuery query) throws NotFoundException {
        return refreshTokenRepository.findById(query.getId()).orElseThrow(() -> new RefreshTokenNotFoundException(query.getId().toString()));
    }
}
