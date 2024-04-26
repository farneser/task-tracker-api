package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<List<Status>> findByProjectIdOrderByOrderNumber(Long userid);

    Optional<Status> findByIdAndProjectId(Long id, Long projectId);

    @Query("SELECT s FROM Status s LEFT JOIN FETCH s.tasks WHERE s.id = :id")
    Optional<Status> findByIdWithTasks(Long id);
}