package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.DatabaseInitializationExtension;
import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import dev.farneser.tasktracker.api.service.auth.CustomUserAuthentication;
import dev.farneser.tasktracker.api.service.auth.UserAuthentication;
import dev.farneser.tasktracker.api.web.dto.project.CreateProjectDto;
import dev.farneser.tasktracker.api.web.dto.project.PatchProjectDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(DatabaseInitializationExtension.class)
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Test
    void create_ValidProjectData_ProjectCreated() throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        CreateProjectDto dto = new CreateProjectDto();

        dto.setProjectName("new project");

        UserAuthentication authentication = new CustomUserAuthentication("user1");

        ProjectView project = projectService.create(dto, authentication);

        assertNotNull(project);
        assertNotNull(project.getId());
    }

    @Test
    void create_InvalidProjectData_ThrowsValidationException() {
        CreateProjectDto dto = new CreateProjectDto();
        dto.setProjectName("");

        UserAuthentication authentication = new CustomUserAuthentication("user1");

        assertThrows(ValidationException.class, () -> projectService.create(dto, authentication));
    }

    @Test
    void get_ValidUser_ReturnsListOfProjects() throws NotFoundException, OperationNotAuthorizedException {
        UserAuthentication authentication = new CustomUserAuthentication("user1");

        List<ProjectView> projectList = projectService.get(authentication);

        assertNotNull(projectList);
        assertFalse(projectList.isEmpty());
    }

    @Test
    void get_InvalidUser_ThrowsNotFoundException() {
        UserAuthentication authentication = new CustomUserAuthentication("user_not_exists");

        assertThrows(NotFoundException.class, () -> projectService.get(authentication));
    }

    @Test
    void patch_ValidProjectData_ProjectPatched() throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserAuthentication authentication = new CustomUserAuthentication("user1");

        ProjectView project = projectService.create(new CreateProjectDto("project"), authentication);

        PatchProjectDto patchProjectDto = new PatchProjectDto();
        patchProjectDto.setProjectName("new project name");

        ProjectView patchedProject = projectService.patch(project.getId(), patchProjectDto, authentication);

        assertNotNull(patchedProject);
        assertEquals(project.getId(), patchedProject.getId());
    }

    @Test
    void patch_InvalidProjectData_ThrowsValidationException() throws ValidationException, NotFoundException, OperationNotAuthorizedException {
        UserAuthentication authentication = new CustomUserAuthentication("user1");

        ProjectView project = projectService.create(new CreateProjectDto("project"), authentication);

        PatchProjectDto patchProjectDto = new PatchProjectDto();
        patchProjectDto.setProjectName("");

        assertThrows(ValidationException.class, () -> projectService.patch(project.getId(), patchProjectDto, authentication));
    }

    @Test
    void delete_ValidProjectId_ProjectDeleted() throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserAuthentication authentication = new CustomUserAuthentication("user1");

        ProjectView project = projectService.create(new CreateProjectDto("delete me project"), authentication);

        projectService.delete(project.getId(), authentication);

        assertThrows(NotFoundException.class, () -> projectService.get(project.getId(), authentication));
    }

    @Test
    void delete_InvalidProjectId_ThrowsNotFoundException() {
        long projectId = 999L;
        UserAuthentication authentication = new CustomUserAuthentication("user1");

        assertThrows(NotFoundException.class, () -> projectService.delete(projectId, authentication));
    }
}
