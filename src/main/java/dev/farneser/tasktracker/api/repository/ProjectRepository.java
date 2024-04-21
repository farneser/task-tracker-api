package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}