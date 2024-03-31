package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<List<Task>> findByStatusIdOrderByOrderNumber(Long id);

//    Optional<List<Task>> findByUserIdAndColumnIdOrderByOrderNumber(Long id, Long columnId);

//    Optional<Task> findByIdAndUserId(Long id, Long userId);
}