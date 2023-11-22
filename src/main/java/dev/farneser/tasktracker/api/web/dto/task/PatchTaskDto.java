package dev.farneser.tasktracker.api.web.dto.task;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.task.patch.PatchTaskCommand;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class PatchTaskDto implements ITypeMapper {
    private String taskName;
    private String description;
    private Long columnId;
    private Long orderNumber;

    @Override
    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(PatchTaskDto.class, PatchTaskCommand.class)
                .addMapping(PatchTaskDto::getTaskName, PatchTaskCommand::setTitle)
                .addMapping(PatchTaskDto::getDescription, PatchTaskCommand::setDescription)
                .addMapping(PatchTaskDto::getColumnId, PatchTaskCommand::setColumnId)
                .addMapping(PatchTaskDto::getOrderNumber, PatchTaskCommand::setOrderNumber);
    }
}
