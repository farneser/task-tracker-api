package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.DatabaseInitializationExtension;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.models.project.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(DatabaseInitializationExtension.class)
public class StatusRepositoryTest {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testFindByProjectIdOrderByOrderNumber() {
        Project project = Project
                .builder()
                .projectName("My Project")
                .build();

        projectRepository.save(project);

        Status status1 = new Status();
        status1.setProject(project);
        status1.setStatusName("To Do");
        status1.setStatusColor("#FF0000");
        status1.setIsCompleted(false);
        status1.setOrderNumber(1L);
        status1.setCreationDate(new Date());
        status1.setEditDate(new Date());

        Status status2 = new Status();
        status2.setProject(project);
        status2.setStatusName("In Progress");
        status2.setStatusColor("#FFFF00");
        status2.setIsCompleted(false);
        status2.setOrderNumber(2L);
        status2.setCreationDate(new Date());
        status2.setEditDate(new Date());

        statusRepository.save(status1);
        statusRepository.save(status2);

        List<Status> statuses = statusRepository.findByProjectIdOrderByOrderNumber(project.getId()).orElseThrow();

        assertEquals(2, statuses.size());
    }

    @Test
    public void testFindByIdAndProjectId() {
        Project project = Project
                .builder()
                .projectName("My Project")
                .build();

        projectRepository.save(project);

        Status status = new Status();
        status.setProject(project);
        status.setStatusName("To Do");
        status.setStatusColor("#FF0000");
        status.setIsCompleted(false);
        status.setOrderNumber(1L);
        status.setCreationDate(new Date());
        status.setEditDate(new Date());

        statusRepository.save(status);

        Optional<Status> result = statusRepository.findByIdAndProjectId(status.getId(), project.getId());

        assertTrue(result.isPresent());

        assertEquals(status.getId(), result.get().getId());
    }

    @Test
    public void testDeleteStatus() {
        Status status = new Status();
        status.setProject(null);
        status.setStatusName("To Do");
        status.setStatusColor("#FF0000");
        status.setIsCompleted(false);
        status.setOrderNumber(1L);
        status.setCreationDate(new Date());
        status.setEditDate(new Date());

        statusRepository.save(status);

        statusRepository.delete(status);

        Optional<Status> result = statusRepository.findById(status.getId());

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByNonExistingProjectId() {
        List<Status> statuses = statusRepository
                .findByProjectIdOrderByOrderNumber(999L)
                .orElse(List.of());

        assertEquals(0, statuses.size());
    }
}


