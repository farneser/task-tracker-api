package dev.farneser.tasktracker.api.operations.queries.projectinvitetoken.getbyprojectid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.ProjectInviteTokenView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetProjectInviteTokenByProjectIdQuery implements Query<ProjectInviteTokenView> {
    private Long userId;
    private Long projectId;
}
