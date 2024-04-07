package dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.RefreshTokenNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.tokens.RefreshToken;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetRefreshTokenByUserIdQueryHandler implements QueryHandler<GetRefreshTokenByUserIdQuery, RefreshToken> {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken handle(GetRefreshTokenByUserIdQuery query) throws NotFoundException {
        List<RefreshToken> tokens = refreshTokenRepository.findByUserId(query.getId()).orElseThrow(() -> new RefreshTokenNotFoundException(query.getId()));

        log.debug("Tokens found: {}", tokens);

        if (tokens.isEmpty()) {
            log.debug("Tokens empty: {}", tokens);
            throw new RefreshTokenNotFoundException(query.getId());
        }

        log.debug("Tokens not empty: {}", tokens);

        return tokens.get(tokens.size() - 1);
    }
}
