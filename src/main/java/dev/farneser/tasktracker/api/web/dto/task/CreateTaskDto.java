package dev.farneser.tasktracker.api.web.dto.task;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.task.create.CreateTaskCommand;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class CreateTaskDto implements ITypeMapper {
    private Long columnId;
    private String title;
    private String description;

    @Override
    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(CreateTaskDto.class, CreateTaskCommand.class)
                .addMapping(CreateTaskDto::getColumnId, CreateTaskCommand::setColumnId)
                .addMapping(CreateTaskDto::getTitle, CreateTaskCommand::setTitle)
                .addMapping(CreateTaskDto::getDescription, CreateTaskCommand::setDescription);
    }
}
