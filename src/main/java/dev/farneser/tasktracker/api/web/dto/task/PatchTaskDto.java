package dev.farneser.tasktracker.api.web.dto.task;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.task.patch.PatchTaskCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "PatchTaskDto", description = "Patch task DTO")
public class PatchTaskDto implements ITypeMapper {
    @Length(min = 1, max = 255)
    @Schema(name = "taskName", description = "Task name", example = "Do something")
    private String taskName;
    @Schema(name = "description", description = "Task description", example = "Do something with something")
    private String description;
    @Schema(name = "columnId", description = "Column id", example = "1")
    private Long columnId;
    @Schema(name = "orderNumber", description = "Task order number", example = "1")
    private Long orderNumber;

    @Override
    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping PatchTaskDto");

        modelMapper.createTypeMap(PatchTaskDto.class, PatchTaskCommand.class)
                .addMapping(PatchTaskDto::getTaskName, PatchTaskCommand::setTaskName)
                .addMapping(PatchTaskDto::getDescription, PatchTaskCommand::setDescription)
                .addMapping(PatchTaskDto::getColumnId, PatchTaskCommand::setColumnId)
                .addMapping(PatchTaskDto::getOrderNumber, PatchTaskCommand::setOrderNumber);
    }
}
