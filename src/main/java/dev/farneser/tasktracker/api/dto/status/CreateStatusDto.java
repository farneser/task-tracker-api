package dev.farneser.tasktracker.api.dto.status;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.status.create.CreateStatusCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "CreateStatusDto", description = "Create status DTO")
public class CreateStatusDto implements ITypeMapper {
    @Length(min = 1, max = 255)
    @Schema(name = "statusName", description = "Status name", example = "To do")
    private String statusName;
    @Length(min = 7, max = 7)
    @Schema(name = "statusColor", description = "Status color in hex format", example = "#ff00ff")
    private String statusColor;
    @Schema(name = "isCompleted", description = "Is status completed", example = "false")
    private Boolean isCompleted;
    @Schema(name = "projectId", description = "Project id", example = "12")
    private Long projectId;

    @Override
    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping CreateStatusDto");

        modelMapper.createTypeMap(CreateStatusDto.class, CreateStatusCommand.class)
                .addMapping(CreateStatusDto::getStatusName, CreateStatusCommand::setStatusName)
                .addMapping(CreateStatusDto::getIsCompleted, CreateStatusCommand::setIsCompleted)
                .addMapping(CreateStatusDto::getStatusColor, CreateStatusCommand::setStatusColor)
                .addMapping(CreateStatusDto::getProjectId, CreateStatusCommand::setProjectId);
    }
}
