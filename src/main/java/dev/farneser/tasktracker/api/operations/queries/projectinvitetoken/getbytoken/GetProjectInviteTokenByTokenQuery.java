package dev.farneser.tasktracker.api.operations.queries.projectinvitetoken.getbytoken;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.ProjectInviteTokenView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetProjectInviteTokenByTokenQuery implements Query<ProjectInviteTokenView> {
    private String token;
}
