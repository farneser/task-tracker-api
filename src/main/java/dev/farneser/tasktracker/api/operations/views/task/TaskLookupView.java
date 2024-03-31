package dev.farneser.tasktracker.api.operations.views.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Data
@Slf4j
@Schema(name = "TaskView", description = "Task view")
public class TaskLookupView implements ITypeMapper {
    @Schema(name = "id", description = "Task id", example = "1")
    private Long id;
    @Schema(name = "taskName", description = "Task name", example = "Do something")
    private String taskName;
    @Schema(name = "description", description = "Task description", example = "Do something with something")
    private String description;
    @Schema(name = "orderNumber", description = "Task order number", example = "1")
    private Long orderNumber;
    @Schema(name = "columnId", description = "Task column id")
    private Long columnId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(name = "creationDate", description = "Task creation date", example = "2021-01-01T00:00:00.000Z")
    private Date creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(name = "editDate", description = "Task edit date", example = "2021-01-01T00:00:00.000Z")
    private Date editDate;

    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping TaskView");

        modelMapper.createTypeMap(Task.class, TaskLookupView.class)
                .addMapping(Task::getId, TaskLookupView::setId)
                .addMapping(Task::getTaskName, TaskLookupView::setTaskName)
                .addMapping(Task::getDescription, TaskLookupView::setDescription)
                .addMapping(Task::getOrderNumber, TaskLookupView::setOrderNumber)
                .addMapping(Task::getCreationDate, TaskLookupView::setCreationDate)
                .addMapping(Task::getEditDate, TaskLookupView::setEditDate)
                .addMapping(task -> task.getStatus().getId(), TaskLookupView::setColumnId);
    }
}
