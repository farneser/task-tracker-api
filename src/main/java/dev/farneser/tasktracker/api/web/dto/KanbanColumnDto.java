package dev.farneser.tasktracker.api.web.dto;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.kanbancolumn.create.CreateKanbanColumnCommand;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class KanbanColumnDto implements ITypeMapper {
    private String columnName;
    private Boolean isCompleted;

    @Override
    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(KanbanColumnDto.class, CreateKanbanColumnCommand.class)
                .addMapping(KanbanColumnDto::getColumnName, CreateKanbanColumnCommand::setColumnName)
                .addMapping(KanbanColumnDto::getIsCompleted, CreateKanbanColumnCommand::setIsCompleted);

    }
}
