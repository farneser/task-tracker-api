package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.project.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testSaveProject() {
        Project project = Project.builder()
                .projectName("My Project")
                .build();

        projectRepository.save(project);

        Optional<Project> retrievedProject = projectRepository.findById(project.getId());

        assertTrue(retrievedProject.isPresent());
        assertEquals("My Project", retrievedProject.get().getProjectName());
    }

    @Test
    public void testFindAllProjects() {
        Project project1 = Project.builder().projectName("Project 1").build();
        Project project2 = Project.builder().projectName("Project 2").build();

        projectRepository.save(project1);
        projectRepository.save(project2);

        List<Project> projects = projectRepository.findAll();

        assertTrue(projects.size() >= 2);

        assertTrue(projects.stream().anyMatch(p -> p.getId().equals(project1.getId())));
        assertTrue(projects.stream().anyMatch(p -> p.getId().equals(project2.getId())));
    }
}
