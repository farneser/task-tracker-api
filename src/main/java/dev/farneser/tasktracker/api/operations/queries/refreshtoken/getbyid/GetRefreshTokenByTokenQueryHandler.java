package dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.RefreshTokenNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.tokens.RefreshToken;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetRefreshTokenByTokenQueryHandler implements QueryHandler<GetRefreshTokenByTokenQuery, RefreshToken> {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken handle(GetRefreshTokenByTokenQuery query) throws NotFoundException {
        log.debug("Query: {}", query);

        return refreshTokenRepository.findRefreshTokenByToken(query.getToken()).orElseThrow(() -> new RefreshTokenNotFoundException(query.getToken()));
    }
}
