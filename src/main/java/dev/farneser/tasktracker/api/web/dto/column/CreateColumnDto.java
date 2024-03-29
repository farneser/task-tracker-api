package dev.farneser.tasktracker.api.web.dto.column;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.column.create.CreateColumnCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "CreateColumnDto", description = "Create column DTO")
public class CreateColumnDto implements ITypeMapper {
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

        modelMapper.createTypeMap(CreateColumnDto.class, CreateColumnCommand.class)
                .addMapping(CreateColumnDto::getColumnName, CreateColumnCommand::setColumnName)
                .addMapping(CreateColumnDto::getIsCompleted, CreateColumnCommand::setIsCompleted)
                .addMapping(CreateColumnDto::getProjectId, CreateColumnCommand::setProjectId);
    }
}
