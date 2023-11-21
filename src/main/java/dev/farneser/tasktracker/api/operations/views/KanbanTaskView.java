package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.KanbanTask;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class KanbanTaskView implements ITypeMapper {
    private Long id;
    private String taskName;
    private String description;
    private Long orderNumber;
    private Long columnId;

    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(KanbanTask.class, KanbanTaskView.class)
                .addMapping(KanbanTask::getId, KanbanTaskView::setId)
                .addMapping(KanbanTask::getTaskName, KanbanTaskView::setTaskName)
                .addMapping(KanbanTask::getDescription, KanbanTaskView::setDescription)
                .addMapping(KanbanTask::getOrderNumber, KanbanTaskView::setOrderNumber)
                // get column id if column exists, otherwise set to -1
                .addMapping(task -> task.getColumn() != null ? task.getColumn().getId() : -1, KanbanTaskView::setColumnId);
    }
}
