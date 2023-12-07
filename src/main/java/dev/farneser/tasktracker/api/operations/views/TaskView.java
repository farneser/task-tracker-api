package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.KanbanTask;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Data
@Slf4j
@Schema(name = "TaskView", description = "Task view")
public class TaskView implements ITypeMapper {
    @Schema(name = "id", description = "Task id", example = "1")
    private Long id;
    @Schema(name = "taskName", description = "Task name", example = "Do something")
    private String taskName;
    @Schema(name = "description", description = "Task description", example = "Do something with something")
    private String description;
    @Schema(name = "orderNumber", description = "Task order number", example = "1")
    private Long orderNumber;
    @Schema(name = "column", description = "Task column")
    private ColumnView column;
    @Schema(name = "creationDate", description = "Task creation date", example = "2021-01-01T00:00:00.000Z")
    private Date creationDate;
    @Schema(name = "editDate", description = "Task edit date", example = "2021-01-01T00:00:00.000Z")
    private Date editDate;

    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping TaskView");

        modelMapper.createTypeMap(KanbanTask.class, TaskView.class)
                .addMapping(KanbanTask::getId, TaskView::setId)
                .addMapping(KanbanTask::getTaskName, TaskView::setTaskName)
                .addMapping(KanbanTask::getDescription, TaskView::setDescription)
                .addMapping(KanbanTask::getOrderNumber, TaskView::setOrderNumber)
                .addMapping(KanbanTask::getCreatiionDate, TaskView::setCreationDate)
                .addMapping(KanbanTask::getEditDate, TaskView::setEditDate)
                .addMapping(task -> modelMapper.map(task.getColumn(), ColumnView.class), TaskView::setColumn);
    }
}
