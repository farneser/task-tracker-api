package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.project.Project;
import dev.farneser.tasktracker.api.models.project.ProjectRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "ProjectView", description = "Project view")
public class ProjectView implements ITypeMapper {
    @Schema(name = "id", description = "Project id", example = "1")
    private Long id;
    @Schema(name = "projectName", description = "Project name", example = "Best project name")
    private String projectName;
    @Schema(name = "role", description = "Your project role", example = "MEMBER", nullable = true)
    private ProjectRole role;

    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(Project.class, ProjectView.class)
                .addMapping(Project::getId, ProjectView::setId)
                .addMapping(Project::getProjectName, ProjectView::setProjectName);
    }
}
