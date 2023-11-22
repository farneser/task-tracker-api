package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.KanbanTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<KanbanTask, Long> {
    Optional<List<KanbanTask>> findByUserIdOrderByOrderNumber(Long id);

    Optional<List<KanbanTask>> findByUserIdAndColumnIdOrderByOrderNumber(Long id, Long columnId);

    Optional<KanbanTask> findByIdAndUserId(Long id, Long userId);
}