package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.KanbanColumn;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ColumnView implements ITypeMapper {
    private Long id;
    private String columnName;
    private Boolean isCompleted;
    private Long orderNumber;

    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(KanbanColumn.class, ColumnView.class)
                .addMapping(KanbanColumn::getId, ColumnView::setId)
                .addMapping(KanbanColumn::getColumnName, ColumnView::setColumnName)
                .addMapping(KanbanColumn::getIsCompleted, ColumnView::setIsCompleted)
                .addMapping(KanbanColumn::getOrderNumber, ColumnView::setOrderNumber);
    }
}
