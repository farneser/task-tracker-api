package dev.farneser.tasktracker.api.web.dto.status;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.status.create.CreateStatusCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "CreateColumnDto", description = "Create column DTO")
public class CreateStatusDto implements ITypeMapper {
    @Length(min = 1, max = 255)
    @Schema(name = "columnName", description = "Column name", example = "To do")
    private String columnName;
    @Schema(name = "isCompleted", description = "Is column completed", example = "false")
    private Boolean isCompleted;
    @Schema(name = "projectId", description = "Project id", example = "12")
    private Long projectId;

    @Override
    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping CreateColumnDto");

        modelMapper.createTypeMap(CreateStatusDto.class, CreateStatusCommand.class)
                .addMapping(CreateStatusDto::getColumnName, CreateStatusCommand::setStatusName)
                .addMapping(CreateStatusDto::getIsCompleted, CreateStatusCommand::setIsCompleted)
                .addMapping(CreateStatusDto::getProjectId, CreateStatusCommand::setProjectId);
    }
}
