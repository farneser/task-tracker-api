package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.KanbanColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColumnRepository extends JpaRepository<KanbanColumn, Long> {
    Optional<List<KanbanColumn>> findByUserIdOrderByOrderNumber(Long id);

    Optional<KanbanColumn> findByIdAndUserId(Long id, Long userId);
}