package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.tokens.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByToken(String token);

    Optional<List<RefreshToken>> findByUserId(Long id);
}