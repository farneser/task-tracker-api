package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.tokens.ProjectInviteToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectInviteTokenRepository extends JpaRepository<ProjectInviteToken, Long> {
    Optional<ProjectInviteToken> findByProjectId(Long projectId);

    Long deleteByProjectId(Long projectId);

    Optional<ProjectInviteToken> findByToken(String token);
}