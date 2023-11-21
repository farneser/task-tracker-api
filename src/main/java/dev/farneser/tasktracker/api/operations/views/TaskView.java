package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.KanbanTask;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class TaskView implements ITypeMapper {
    private Long id;
    private String taskName;
    private String description;
    private Long orderNumber;
    private Long columnId;

    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(KanbanTask.class, TaskView.class)
                .addMapping(KanbanTask::getId, TaskView::setId)
                .addMapping(KanbanTask::getTaskName, TaskView::setTaskName)
                .addMapping(KanbanTask::getDescription, TaskView::setDescription)
                .addMapping(KanbanTask::getOrderNumber, TaskView::setOrderNumber)
                // get column id if column exists, otherwise set to -1
                .addMapping(task -> task.getColumn() != null ? task.getColumn().getId() : -1, TaskView::setColumnId);
    }
}
