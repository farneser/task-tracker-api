package dev.farneser.tasktracker.api.models;


import dev.farneser.tasktracker.api.models.permissions.Role;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Date;
import java.util.List;


@SpringBootTest
public class UserTest {

    @InjectMocks
    private User user;

    @BeforeEach
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
                .role(Role.USER)
                .build();
    }

    @Test
    public void testIsAccountNonExpired() {
        Assertions.assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        Assertions.assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        Assertions.assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testIdGetterAndSetter() {
        Long id = 10L;
        user.setId(id);
        Assertions.assertEquals(id, user.getId());
    }

    @Test
    public void testUsernameGetterAndSetter() {
        String username = "newUsername";
        user.setUsername(username);
        Assertions.assertEquals(username, user.getUsername());
    }

    @Test
    public void testEmailGetterAndSetter() {
        String email = "newemail@example.com";
        user.setEmail(email);
        Assertions.assertEquals(email, user.getEmail());
    }

    @Test
    public void testPasswordGetterAndSetter() {
        String password = "newPassword";
        user.setPassword(password);
        Assertions.assertEquals(password, user.getPassword());
    }

    @Test
    public void testIsSubscribedGetterAndSetter() {
        boolean isSubscribed = false;
        user.setSubscribed(isSubscribed);
        Assertions.assertEquals(isSubscribed, user.isSubscribed());
    }

    @Test
    public void testRegisterDateGetterAndSetter() {
        Date registerDate = new Date();
        user.setRegisterDate(registerDate);
        Assertions.assertEquals(registerDate, user.getRegisterDate());
    }

    @Test
    public void testIsEnabledGetterAndSetter() {
        boolean isEnabled = false;
        user.setEnabled(isEnabled);
        Assertions.assertEquals(isEnabled, user.isEnabled());
    }

    @Test
    public void testIsLockedGetterAndSetter() {
        boolean isLocked = true;
        user.setLocked(isLocked);
        Assertions.assertEquals(isLocked, user.isLocked());
    }

    @Test
    public void testRoleGetterAndSetter() {
        Role newRole = Role.ADMIN;
        user.setRole(newRole);
        Assertions.assertEquals(newRole, user.getRole());
    }

    @Test
    public void testProjectsGetterAndSetter() {
        List<ProjectMember> projects = Collections.emptyList();
        user.setProjects(projects);
        Assertions.assertEquals(projects, user.getProjects());
    }
}
