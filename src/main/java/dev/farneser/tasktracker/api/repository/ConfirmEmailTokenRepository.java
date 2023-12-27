package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.ConfirmEmailToken;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ConfirmEmailTokenRepository {
    // FIXME: 12/5/23 EMAILS_KEY get from env
    private final String emailTokens = "emailTokens";

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Object> hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void save(ConfirmEmailToken confirmEmailToken, Long lifetime) {
        log.debug("Saving confirm email token {} with lifetime {}", confirmEmailToken.getToken(), lifetime);
        hashOperations.put(emailTokens, confirmEmailToken.getToken().toString(), confirmEmailToken);

        log.debug("Setting expiration for confirm email token {}", confirmEmailToken.getToken());
        redisTemplate.expire(emailTokens, lifetime, TimeUnit.MILLISECONDS);
    }

    public ConfirmEmailToken get(UUID id) {
        log.debug("Getting confirm email token {}", id);

        return (ConfirmEmailToken) hashOperations.get(emailTokens, id.toString());
    }

    public void delete(UUID id) {
        log.debug("Deleting confirm email token {}", id);

        hashOperations.delete(emailTokens, id.toString());
    }
}
