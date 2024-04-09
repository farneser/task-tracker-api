package dev.farneser.tasktracker.api.operations.queries.projectinvitetoken.getbyid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.ProjectInviteTokenView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetProjectInviteTokenByIdQuery implements Query<ProjectInviteTokenView> {
    private Long userId;
    private Long tokenId;
}
