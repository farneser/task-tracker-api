package dev.farneser.tasktracker.api.models;

import dev.farneser.tasktracker.api.models.project.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StatusTest {

    private Task task;

    private Status status;

    @BeforeEach
    public void setUp() {
        status = Status.builder()
                .id(1L)
                .statusName("Test Status")
                .statusColor("#FFFFFF")
                .isCompleted(false)
                .project(null)
                .orderNumber(1L)
                .tasks(new ArrayList<>())
                .creationDate(new Date())
                .editDate(new Date())
                .build();
    }

    @Test
    public void testIdGetterAndSetter() {
        Long id = 10L;
        status.setId(id);
        assertEquals(id, status.getId());
    }

    @Test
    public void testStatusNameGetterAndSetter() {
        String statusName = "New Test Status";
        status.setStatusName(statusName);
        assertEquals(statusName, status.getStatusName());
    }

    @Test
    public void testStatusColorGetterAndSetter() {
        String statusColor = "#CCCCCC";
        status.setStatusColor(statusColor);
        assertEquals(statusColor, status.getStatusColor());
    }

    @Test
    public void testIsCompletedGetterAndSetter() {
        boolean isCompleted = true;
        status.setIsCompleted(isCompleted);
        assertEquals(isCompleted, status.getIsCompleted());
    }

    @Test
    public void testProjectGetterAndSetter() {
        Project newProject = new Project();
        status.setProject(newProject);
        assertEquals(newProject, status.getProject());
    }

    @Test
    public void testOrderNumberGetterAndSetter() {
        Long orderNumber = 5L;
        status.setOrderNumber(orderNumber);
        assertEquals(orderNumber, status.getOrderNumber());
    }

    @Test
    public void testTasksGetterAndSetter() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        status.setTasks(tasks);
        assertEquals(tasks, status.getTasks());
    }

    @Test
    public void testCreationDateGetterAndSetter() {
        Date creationDate = new Date();
        status.setCreationDate(creationDate);
        assertEquals(creationDate, status.getCreationDate());
    }

    @Test
    public void testEditDateGetterAndSetter() {
        Date editDate = new Date();
        status.setEditDate(editDate);
        assertEquals(editDate, status.getEditDate());
    }
}
