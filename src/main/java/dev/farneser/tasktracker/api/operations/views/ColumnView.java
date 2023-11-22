package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.KanbanColumn;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;

@Data
public class ColumnView implements ITypeMapper {
    private Long id;
    private String columnName;
    private Boolean isCompleted;
    private Long orderNumber;
    private List<TaskView> tasks;

    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(KanbanColumn.class, ColumnView.class)
                .addMapping(KanbanColumn::getId, ColumnView::setId)
                .addMapping(KanbanColumn::getColumnName, ColumnView::setColumnName)
                .addMapping(KanbanColumn::getIsCompleted, ColumnView::setIsCompleted)
                .addMapping(KanbanColumn::getIsCompleted, ColumnView::setIsCompleted)
                .addMapping(col -> col.getTasks().stream().map(source -> modelMapper.map(source, TaskView.class)).toList(), ColumnView::setTasks);
    }
}
