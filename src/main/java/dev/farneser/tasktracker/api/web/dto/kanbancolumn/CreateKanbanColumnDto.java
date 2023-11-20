package dev.farneser.tasktracker.api.web.dto.kanbancolumn;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.kanbancolumn.create.CreateKanbanColumnCommand;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class CreateKanbanColumnDto implements ITypeMapper {
    private String columnName;
    private Boolean isCompleted;

    @Override
    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(CreateKanbanColumnDto.class, CreateKanbanColumnCommand.class)
                .addMapping(CreateKanbanColumnDto::getColumnName, CreateKanbanColumnCommand::setColumnName)
                .addMapping(CreateKanbanColumnDto::getIsCompleted, CreateKanbanColumnCommand::setIsCompleted);
    }
}
