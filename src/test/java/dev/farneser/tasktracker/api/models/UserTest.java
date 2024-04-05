package dev.farneser.tasktracker.api.models;


import dev.farneser.tasktracker.api.models.permissions.Role;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

    @Mock
    private Role role;

    @Mock
    private ProjectMember projectMember;

    @InjectMocks
    private User user;

    @Before
    public void setUp() {
        user = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .password("password")
                .isSubscribed(true)
                .registerDate(new Date())
                .isEnabled(true)
                .isLocked(false)
                .role(role)
                .projects(Collections.singletonList(projectMember))
                .build();
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testIdGetterAndSetter() {
        Long id = 10L;
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testUsernameGetterAndSetter() {
        String username = "newUsername";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testEmailGetterAndSetter() {
        String email = "newemail@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testPasswordGetterAndSetter() {
        String password = "newPassword";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testIsSubscribedGetterAndSetter() {
        boolean isSubscribed = false;
        user.setSubscribed(isSubscribed);
        assertEquals(isSubscribed, user.isSubscribed());
    }

    @Test
    public void testRegisterDateGetterAndSetter() {
        Date registerDate = new Date();
        user.setRegisterDate(registerDate);
        assertEquals(registerDate, user.getRegisterDate());
    }

    @Test
    public void testIsEnabledGetterAndSetter() {
        boolean isEnabled = false;
        user.setEnabled(isEnabled);
        assertEquals(isEnabled, user.isEnabled());
    }

    @Test
    public void testIsLockedGetterAndSetter() {
        boolean isLocked = true;
        user.setLocked(isLocked);
        assertEquals(isLocked, user.isLocked());
    }

    @Test
    public void testRoleGetterAndSetter() {
        Role newRole = Role.ADMIN;
        user.setRole(newRole);
        assertEquals(newRole, user.getRole());
    }

    @Test
    public void testProjectsGetterAndSetter() {
        List<ProjectMember> projects = Collections.emptyList();
        user.setProjects(projects);
        assertEquals(projects, user.getProjects());
    }
}
