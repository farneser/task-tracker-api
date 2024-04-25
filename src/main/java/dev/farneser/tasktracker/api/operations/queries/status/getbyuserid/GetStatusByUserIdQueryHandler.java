package dev.farneser.tasktracker.api.operations.queries.status.getbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ProjectMemberNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.operations.views.StatusView;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetStatusByUserIdQueryHandler implements QueryHandler<GetStatusByUserIdQuery, List<StatusView>> {
    private final StatusRepository statusRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<StatusView> handle(GetStatusByUserIdQuery query)
            throws NotFoundException, OperationNotAuthorizedException {

        Set<Long> projectIds = new HashSet<>();

        if (query.getProjectId() == -1L) {
            List<ProjectMember> projectMembers = projectMemberRepository
                    .findByMemberId(query.getUserId())
                    .orElse(new ArrayList<>());

            projectIds.addAll(projectMembers.stream().map(p -> p.getProject().getId()).toList());

        } else {
            projectIds.add(query.getProjectId());
        }

        List<StatusView> result = new ArrayList<>();

        for (Long id : projectIds) {
            ProjectMember member = projectMemberRepository
                    .findByProjectIdAndMemberId(id, query.getUserId())
                    .orElseThrow(() -> new ProjectMemberNotFoundException(query.getUserId()));

            if (!member.getRole().hasPermission(ProjectPermission.USER_GET)) {
                throw new OperationNotAuthorizedException();
            }

            List<Status> statuses = statusRepository.findByProjectIdOrderByOrderNumber(id).orElse(new ArrayList<>());

            log.debug("Status found: {}", statuses);

            List<StatusView> views = statuses.stream().map(c -> modelMapper.map(c, StatusView.class)).toList();
            result.addAll(views);
        }

        log.debug("Status mapped: {}", Arrays.toString(result.toArray()));

        if (!query.getRetrieveTasks()) {
            result.forEach(c -> c.setTasks(null));
        }

        return result;
    }
}
