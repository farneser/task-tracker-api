package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.ConfirmEmailToken;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class ConfirmEmailTokenRepository {
    // FIXME: 12/5/23 EMAILS_KEY get from env
    private final String EMAIL_TOKENS = "emailTokens";

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Object> hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void save(ConfirmEmailToken confirmEmailToken) {
        hashOperations.put(EMAIL_TOKENS, confirmEmailToken.getEmail(), confirmEmailToken);
        redisTemplate.expire(confirmEmailToken.getEmail(), confirmEmailToken.getExpiresAt().getTime(), TimeUnit.SECONDS);
    }

    public ConfirmEmailToken get(String email) {
        return (ConfirmEmailToken) hashOperations.get(EMAIL_TOKENS, email);
    }

    public void delete(String email) {
        hashOperations.delete(EMAIL_TOKENS, email);
    }
}
