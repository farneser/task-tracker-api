package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.KanbanTask;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Data
public class TaskView implements ITypeMapper {
    private Long id;
    private String taskName;
    private String description;
    private Long orderNumber;
    private ColumnView column;
    private Date creationDate;
    private Date editDate;

    public void mapping(ModelMapper modelMapper) {
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
