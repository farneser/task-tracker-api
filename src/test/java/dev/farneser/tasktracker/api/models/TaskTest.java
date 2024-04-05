package dev.farneser.tasktracker.api.models;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TaskTest {

    @Mock
    private User assignedFor;

    @Mock
    private Status status;

    @InjectMocks
    private Task task;

    @Before
    public void setUp() {
        task = Task.builder()
                .id(1L)
                .taskName("Test Task")
                .description("Description for test task")
                .assignedFor(assignedFor)
                .status(status)
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
    public void testCreationDateGetterAndSetter() {
        Date creationDate = new Date();
        task.setCreationDate(creationDate);
        assertEquals(creationDate, task.getCreationDate());
    }

    @Test
    public void testEditDateGetterAndSetter() {
        Date editDate = new Date();
        task.setEditDate(editDate);
        assertEquals(editDate, task.getEditDate());
    }
}
