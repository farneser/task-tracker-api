package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Optional<ProjectMember> findByProjectIdAndMemberId(Long projectId, Long memberId);

    Optional<List<ProjectMember>> findByMemberId(Long memberId);

    Optional<ProjectMember> findByIdAndProjectId(Long id, Long projectId);

    Optional<ProjectMember> findByIdAndMemberId(Long id, Long memberId);
}