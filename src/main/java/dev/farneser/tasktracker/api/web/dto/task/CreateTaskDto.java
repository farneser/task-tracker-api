package dev.farneser.tasktracker.api.web.dto.task;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.task.create.CreateTaskCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "CreateTaskDto", description = "Create task DTO")
public class CreateTaskDto implements ITypeMapper {
    @Schema(name = "columnId", description = "Column id", example = "1")
    private Long columnId;
    @Length(min = 1, max = 255)
    @Schema(name = "taskName", description = "Task name", example = "Do something")
    private String taskName;
    @Schema(name = "description", description = "Task description", example = "Do something with something")
    private String description;

    @Override
    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping CreateTaskDto");

        modelMapper.createTypeMap(CreateTaskDto.class, CreateTaskCommand.class)
                .addMapping(CreateTaskDto::getColumnId, CreateTaskCommand::setColumnId)
                .addMapping(CreateTaskDto::getTaskName, CreateTaskCommand::setTaskName)
                .addMapping(CreateTaskDto::getDescription, CreateTaskCommand::setDescription);
    }
}
