package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColumnRepository extends JpaRepository<Status, Long> {
    Optional<List<Status>> findByProjectIdOrderByOrderNumber(Long userid);

    Optional<Status> findByIdAndProjectId(Long id, Long userId);
}