package dev.farneser.tasktracker.api.web.dto.column;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.column.create.CreateColumnCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
@Schema(name = "CreateColumnDto", description = "Create column DTO")
public class CreateColumnDto implements ITypeMapper {
    @Schema(name = "columnName", description = "Column name", example = "To do")
    private String columnName;
    @Schema(name = "isCompleted", description = "Is column completed", example = "false")
    private Boolean isCompleted;

    @Override
    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(CreateColumnDto.class, CreateColumnCommand.class)
                .addMapping(CreateColumnDto::getColumnName, CreateColumnCommand::setColumnName)
                .addMapping(CreateColumnDto::getIsCompleted, CreateColumnCommand::setIsCompleted);
    }
}
