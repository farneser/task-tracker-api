package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.tokens.ProjectInviteToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "ProjectInviteTokenView", description = "View of project invite token")
public class ProjectInviteTokenView implements ITypeMapper {
    @Schema(name = "token", description = "Invite token", example = "98477d20-6bc5-4a1e-bb92-a745bacc0f81")
    private String token;
    @Schema(name = "email", description = "Project invite token creator email", example = "example@email.com")
    private String email;

    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(ProjectInviteToken.class, ProjectInviteTokenView.class)
                .addMapping(ProjectInviteToken::getToken, ProjectInviteTokenView::setToken);
    }
}
