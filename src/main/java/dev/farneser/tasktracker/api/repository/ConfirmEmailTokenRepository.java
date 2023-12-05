package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.ConfirmEmailToken;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    public void save(ConfirmEmailToken confirmEmailToken) {
        hashOperations.put(emailTokens, confirmEmailToken.getToken().toString(), confirmEmailToken);
        redisTemplate.expire(confirmEmailToken.getToken().toString(), confirmEmailToken.getExpiresAt().getTime(), TimeUnit.SECONDS);
    }

    public ConfirmEmailToken get(UUID id) {
        return (ConfirmEmailToken) hashOperations.get(emailTokens, id.toString());
    }

    public void delete(UUID id) {
        hashOperations.delete(emailTokens, id.toString());
    }
}
