package dev.farneser.tasktracker.api.operations.queries.task;

import dev.farneser.tasktracker.api.models.KanbanTask;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    private final ModelMapper modelMapper;

    public List<TaskLookupView> mapTaskToTaskLookupView(List<KanbanTask> tasks) {
        ArrayList<TaskLookupView> result = new ArrayList<>();

        tasks.forEach(task -> {
            TaskLookupView view = modelMapper.map(task, TaskLookupView.class);

            if (task.getColumn() != null) {
                view.setColumnId(task.getColumn().getId());
            } else {
                view.setColumnId(-1L);
            }

            result.add(view);
        });

        return result;
    }
}
