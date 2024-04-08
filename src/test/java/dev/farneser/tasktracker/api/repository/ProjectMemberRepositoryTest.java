package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.models.permissions.Role;
import dev.farneser.tasktracker.api.models.project.Project;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ProjectMemberRepositoryTest {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindProjectMemberByProjectIdAndMemberId() {
        User user = User.builder()
                .username("testFindProjectMemberByProjectIdAndMemberId")
                .email("testFindProjectMemberByProjectIdAndMemberId@example.com")
                .password("password")
                .isSubscribed(true)
                .isEnabled(true)
                .registerDate(new Date())
                .isLocked(false)
                .role(Role.USER)
                .build();
        userRepository.save(user);

        Project project = Project.builder().projectName("Test Project").build();
        projectRepository.save(project);

        ProjectMember projectMember = ProjectMember.builder()
                .member(user)
                .project(project)
                .role(ProjectRole.MEMBER)
                .build();
        projectMemberRepository.save(projectMember);

        Optional<ProjectMember> retrievedProjectMember = projectMemberRepository
                .findProjectMemberByProjectIdAndMemberId(project.getId(), user.getId());

        assertTrue(retrievedProjectMember.isPresent());
        assertEquals(projectMember.getId(), retrievedProjectMember.get().getId());
    }

    @Test
    public void testFindProjectMemberByMemberId() {
        User user = User.builder()
                .username("testFindProjectMemberByMemberId")
                .email("testFindProjectMemberByMemberId@example.com")
                .password("password")
                .registerDate(new Date())
                .isSubscribed(true)
                .isEnabled(true)
                .isLocked(false)
                .role(Role.USER)
                .build();

        userRepository.save(user);

        ProjectMember projectMember = ProjectMember.builder()
                .member(user)
                .role(ProjectRole.MEMBER)
                .build();
        projectMemberRepository.save(projectMember);

        Optional<List<ProjectMember>> projectMembers = projectMemberRepository.findProjectMemberByMemberId(user.getId());

        assertTrue(projectMembers.isPresent());
        assertEquals(1, projectMembers.get().size());
        assertEquals(projectMember.getId(), projectMembers.get().get(0).getId());
    }
}
