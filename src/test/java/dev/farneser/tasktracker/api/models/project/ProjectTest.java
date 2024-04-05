package dev.farneser.tasktracker.api.models.project;

import dev.farneser.tasktracker.api.models.Status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ProjectTest {

    @Mock
    private Status status;

    @Mock
    private ProjectMember projectMember;

    @InjectMocks
    private Project project;

    @Before
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
