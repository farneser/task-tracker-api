package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "ProjectMemberView", description = "Project member view")
public class ProjectMemberView implements ITypeMapper {
    @Schema(name = "id", description = "Project member id", example = "1")
    private Long id;
    private String projectId;
    private UserView member;
    private ProjectRole role;

    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(ProjectMember.class, ProjectMemberView.class)
                .addMapping(ProjectMember::getId, ProjectMemberView::setId)
                .addMapping(p -> p.getProject().getId(), ProjectMemberView::setProjectId)
                .addMapping(ProjectMember::getRole, ProjectMemberView::setRole);
    }
}
