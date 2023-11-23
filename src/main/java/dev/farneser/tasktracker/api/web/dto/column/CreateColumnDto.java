package dev.farneser.tasktracker.api.web.dto.column;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.column.create.CreateColumnCommand;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class CreateColumnDto implements ITypeMapper {
    private String columnName;
    private Boolean isCompleted;

    @Override
    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(CreateColumnDto.class, CreateColumnCommand.class)
                .addMapping(CreateColumnDto::getColumnName, CreateColumnCommand::setColumnName)
                .addMapping(CreateColumnDto::getIsCompleted, CreateColumnCommand::setIsCompleted);
    }
}
