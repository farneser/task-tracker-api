package dev.farneser.tasktracker.api.models.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProjectTest {

    @InjectMocks
    private Project project;

    @BeforeEach
    public void setUp() {
        project = new Project();
        project.setId(1L);
        project.setProjectName("Test Project");
    }

    @Test
    public void testIdGetterAndSetter() {
        Long id = 10L;
        project.setId(id);
        assertEquals(id, project.getId());
    }

    @Test
    public void testProjectNameGetterAndSetter() {
        String projectName = "New Test Project";
        project.setProjectName(projectName);
        assertEquals(projectName, project.getProjectName());
    }
}
