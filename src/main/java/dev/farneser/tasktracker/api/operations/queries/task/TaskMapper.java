package dev.farneser.tasktracker.api.operations.queries.task;

import dev.farneser.tasktracker.api.models.KanbanTask;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    private final ModelMapper modelMapper;

    public List<TaskView> mapTaskToTaskView(List<KanbanTask> tasks) {
        var view = tasks.stream().map(task -> modelMapper.map(task, TaskView.class)).toList();

        view.forEach(task -> {
            if (task.getColumn() != null && task.getColumn().getTasks() != null) {
                task.getColumn().getTasks().forEach(t -> t.setColumn(null));
            }
        });

        return view;
    }
}
