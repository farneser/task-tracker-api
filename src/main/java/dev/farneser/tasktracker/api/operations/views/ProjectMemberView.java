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
    @Schema(name = "projectId", description = "Project id", example = "1")
    private Long projectId;
    private Long userId;
    private String username;
    private String email;
    private ProjectRole role;

    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(ProjectMember.class, ProjectMemberView.class)
                .addMapping(p -> p.getProject().getId(), ProjectMemberView::setProjectId)
                .addMapping(p -> p.getMember().getId(), ProjectMemberView::setUserId)
                .addMapping(p -> p.getMember().getUsername(), ProjectMemberView::setUsername)
                .addMapping(p -> p.getMember().getEmail(), ProjectMemberView::setEmail)
                .addMapping(ProjectMember::getRole, ProjectMemberView::setRole);
    }

    public static ProjectMemberView map(ProjectMember model) {
        ProjectMemberView view = new ProjectMemberView();

        view.setProjectId(model.getProject().getId());
        view.setRole(model.getRole());
        view.setUserId(model.getMember().getId());
        view.setEmail(model.getMember().getEmail());
        view.setUsername(model.getMember().getUsername());

        return view;
    }
}
