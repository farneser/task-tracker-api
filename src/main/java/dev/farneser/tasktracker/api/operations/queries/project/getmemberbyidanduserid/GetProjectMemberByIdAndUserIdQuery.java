package dev.farneser.tasktracker.api.operations.queries.project.getmemberbyidanduserid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.ProjectMemberView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetProjectMemberByIdAndUserIdQuery implements Query<ProjectMemberView> {
    private Long projectMemberId;
    private Long userId;
}
