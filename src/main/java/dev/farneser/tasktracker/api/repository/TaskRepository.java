package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<List<Task>> findByStatusIdOrderByOrderNumber(Long id);

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND (t.status.id < 1 OR t.status IS NULL)")
    Optional<List<Task>> findByProjectIdAndStatusLessThanOneOrNull(Long projectId);
}