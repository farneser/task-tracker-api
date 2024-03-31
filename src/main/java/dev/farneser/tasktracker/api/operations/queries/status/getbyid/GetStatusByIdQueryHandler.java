package dev.farneser.tasktracker.api.operations.queries.status.getbyid;

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

@Slf4j
@Component
@RequiredArgsConstructor
public class GetStatusByIdQueryHandler implements QueryHandler<GetStatusByIdQuery, StatusView> {
    private final StatusRepository statusRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ModelMapper modelMapper;

    @Override
    public StatusView handle(GetStatusByIdQuery query) throws NotFoundException, OperationNotAuthorizedException {

        Status column = statusRepository
                .findById(query.getStatusId())
                .orElseThrow(() -> new NotFoundException("Status with id " + query.getStatusId() + " not found"));

        log.debug("Column found: {}", column);

        ProjectMember member = projectMemberRepository
                .findProjectMemberByProjectIdAndMemberId(column.getProject().getId(), query.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.USER_GET)) {
            throw new OperationNotAuthorizedException();
        }

        StatusView view = modelMapper.map(column, StatusView.class);

        log.debug("Column mapped: {}", view);

        return view;
    }
}
