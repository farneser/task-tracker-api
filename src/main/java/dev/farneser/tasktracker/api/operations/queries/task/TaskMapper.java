package dev.farneser.tasktracker.api.operations.queries.task;

import dev.farneser.tasktracker.api.models.Task;
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

    public List<TaskLookupView> mapTaskToTaskLookupView(List<Task> tasks) {
        ArrayList<TaskLookupView> result = new ArrayList<>();

        tasks.forEach(task -> {
            TaskLookupView view = modelMapper.map(task, TaskLookupView.class);

            view.setStatusId(task.getStatus() != null ? task.getStatus().getId() : -1L);
            view.setAssignedUserId(task.getAssignedFor() == null ? -1 : task.getAssignedFor().getId());

            result.add(view);
        });

        return result;
    }
}
