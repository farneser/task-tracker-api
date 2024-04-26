package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.models.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TasksRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Test
    void testFindByStatusIdOrderByOrderNumber() {
        Status status1 = new Status();
        status1.setStatusName("To Do");
        status1.setStatusColor("#FF0000");
        status1.setIsCompleted(false);
        status1.setOrderNumber(1L);
        status1.setCreationDate(new Date());
        status1.setEditDate(new Date());

        statusRepository.save(status1);

        Status status2 = new Status();
        status2.setStatusName("In Progress");
        status2.setStatusColor("#00FF00");
        status2.setIsCompleted(false);
        status2.setOrderNumber(2L);
        status2.setCreationDate(new Date());
        status2.setEditDate(new Date());

        statusRepository.save(status2);

        Date date = new Date(System.currentTimeMillis());

        Task task1 = new Task();
        task1.setStatus(status1);
        task1.setOrderNumber(1L);
        task1.setTaskName("name");
        task1.setDescription("description");
        task1.setCreationDate(date);
        task1.setEditDate(date);

        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setStatus(status1);
        task2.setOrderNumber(2L);
        task2.setTaskName("name");
        task2.setDescription("description");
        task2.setCreationDate(date);
        task2.setEditDate(date);

        taskRepository.save(task2);

        Task task3 = new Task();
        task3.setStatus(status2);
        task3.setOrderNumber(1L);
        task3.setTaskName("name");
        task3.setDescription("description");
        task3.setCreationDate(date);
        task3.setEditDate(date);

        taskRepository.save(task3);

        Optional<List<Task>> tasksByStatusId = taskRepository.findByStatusIdOrderByOrderNumber(status1.getId());

        Optional<Task> task1ById = taskRepository.findById(task1.getId());
        Optional<Task> task2ById = taskRepository.findById(task2.getId());

        assertTrue(tasksByStatusId.isPresent());
        assertTrue(task1ById.isPresent());
        assertTrue(task2ById.isPresent());

        List<Task> tasks = tasksByStatusId.get();

        assertEquals(2, tasks.size());

        assertEquals(task1ById.get().getId(), tasks.get(0).getId());
        assertEquals(task2ById.get().getId(), tasks.get(1).getId());
    }

    @Test
    void testFindByStatusIdOrderByOrderNumber_NoTasksFound() {
        Optional<List<Task>> tasksByStatusId = taskRepository.findByStatusIdOrderByOrderNumber(1L);

        assertFalse(tasksByStatusId.isEmpty());
        assertTrue(tasksByStatusId.get().isEmpty());
    }
}
