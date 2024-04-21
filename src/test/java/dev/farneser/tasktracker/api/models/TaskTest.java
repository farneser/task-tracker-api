package dev.farneser.tasktracker.api.models;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TaskTest {

    private Task task;

    @BeforeEach
    public void setUp() {
        task = Task.builder()
                .id(1L)
                .taskName("Test Task")
                .description("Description for test task")
                .assignedFor(null)
                .status(null)
                .orderNumber(1L)
                .creationDate(new Date())
                .editDate(new Date())
                .build();
    }

    @Test
    public void testIdGetterAndSetter() {
        Long id = 10L;
        task.setId(id);
        assertEquals(id, task.getId());
    }

    @Test
    public void testTaskNameGetterAndSetter() {
        String taskName = "New Test Task";
        task.setTaskName(taskName);
        assertEquals(taskName, task.getTaskName());
    }

    @Test
    public void testDescriptionGetterAndSetter() {
        String description = "New description for test task";
        task.setDescription(description);
        assertEquals(description, task.getDescription());
    }

    @Test
    public void testAssignedForGetterAndSetter() {
        User newUser = new User();
        task.setAssignedFor(newUser);
        assertEquals(newUser, task.getAssignedFor());
    }

    @Test
    public void testStatusGetterAndSetter() {
        Status newStatus = new Status();
        task.setStatus(newStatus);
        assertEquals(newStatus, task.getStatus());
    }

    @Test
    public void testOrderNumberGetterAndSetter() {
        Long orderNumber = 5L;
        task.setOrderNumber(orderNumber);
        assertEquals(orderNumber, task.getOrderNumber());
    }

    @Test
    public void testCrDateGetterAndSetter() {
        Date editDate = new Date();
        task.setEditDate(editDate);
        assertEquals(editDate, task.getEditDate());
    }

    @Test
    public void testEditDateGetterAndSetter() {
        Date editDate = new Date();
        task.setEditDate(editDate);
        assertEquals(editDate, task.getEditDate());
    }
}
