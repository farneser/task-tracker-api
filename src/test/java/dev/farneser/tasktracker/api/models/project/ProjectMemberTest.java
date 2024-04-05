package dev.farneser.tasktracker.api.models.project;

import dev.farneser.tasktracker.api.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ProjectMemberTest {

    @Mock
    private User user;

    @Mock
    private Project project;

    @InjectMocks
    private ProjectMember projectMember;

    @Before
    public void setUp() {
        projectMember = new ProjectMember();
        projectMember.setId(1L);
        projectMember.setMember(user);
        projectMember.setProject(project);
        projectMember.setRole(ProjectRole.MEMBER);
    }

    @Test
    public void testIdGetterAndSetter() {
        Long id = 10L;
        projectMember.setId(id);
        assertEquals(id, projectMember.getId());
    }

    @Test
    public void testMemberGetterAndSetter() {
        User newUser = new User();
        projectMember.setMember(newUser);
        assertEquals(newUser, projectMember.getMember());
    }

    @Test
    public void testProjectGetterAndSetter() {
        Project newProject = new Project();
        projectMember.setProject(newProject);
        assertEquals(newProject, projectMember.getProject());
    }

    @Test
    public void testRoleGetterAndSetter() {
        ProjectRole role = ProjectRole.ADMIN;
        projectMember.setRole(role);
        assertEquals(role, projectMember.getRole());
    }
}
