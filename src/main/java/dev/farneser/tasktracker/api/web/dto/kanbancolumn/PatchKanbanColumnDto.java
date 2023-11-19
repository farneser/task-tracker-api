package dev.farneser.tasktracker.api.web.dto.kanbancolumn;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.kanbancolumn.patch.PatchKanbanColumnCommand;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class PatchKanbanColumnDto implements ITypeMapper {
    private String columnName;
    private Boolean isCompleted;
    private Long orderNumber;

    @Override
    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(PatchKanbanColumnDto.class, PatchKanbanColumnCommand.class)
                .addMapping(PatchKanbanColumnDto::getColumnName, PatchKanbanColumnCommand::setColumnName)
                .addMapping(PatchKanbanColumnDto::getIsCompleted, PatchKanbanColumnCommand::setIsCompleted)
                .addMapping(PatchKanbanColumnDto::getOrderNumber, PatchKanbanColumnCommand::setOrderNumber);
    }
}
