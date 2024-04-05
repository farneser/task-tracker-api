package dev.farneser.tasktracker.api.models.project;

import dev.farneser.tasktracker.api.models.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProjectTest {

    @Mock
    private Status status;

    @Mock
    private ProjectMember projectMember;

    @InjectMocks
    private Project project;

    @BeforeEach
    public void setUp() {
        project = new Project();
        project.setId(1L);
        project.setProjectName("Test Project");
        List<Status> statuses = new ArrayList<>();
        statuses.add(status);
        project.setStatuses(statuses);
        List<ProjectMember> members = new ArrayList<>();
        members.add(projectMember);
        project.setMembers(members);
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

    @Test
    public void testStatusesGetterAndSetter() {
        List<Status> statuses = new ArrayList<>();
        statuses.add(status);
        project.setStatuses(statuses);
        assertEquals(statuses, project.getStatuses());
    }

    @Test
    public void testMembersGetterAndSetter() {
        List<ProjectMember> members = new ArrayList<>();
        members.add(projectMember);
        project.setMembers(members);
        assertEquals(members, project.getMembers());
    }
}
