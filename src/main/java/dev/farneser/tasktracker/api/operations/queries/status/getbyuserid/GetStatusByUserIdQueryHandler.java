package dev.farneser.tasktracker.api.operations.queries.status.getbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        ProjectMember member = projectMemberRepository
                .findProjectMemberByProjectIdAndMemberId(query.getProjectId(), query.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.USER_GET)) {
            throw new OperationNotAuthorizedException();
        }

        List<Status> columns = statusRepository.findByProjectIdOrderByOrderNumber(query.getProjectId()).orElse(new ArrayList<>());

        log.debug("Column found: {}", columns);

        List<StatusView> views = columns.stream().map(c -> modelMapper.map(c, StatusView.class)).toList();

        log.debug("Column mapped: {}", Arrays.toString(views.toArray()));

        if (!query.getRetrieveTasks()) {
            views.forEach(c -> c.setTasks(null));
        }

        return views;
    }
}
