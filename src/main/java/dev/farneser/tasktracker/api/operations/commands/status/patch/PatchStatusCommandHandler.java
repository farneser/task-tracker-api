package dev.farneser.tasktracker.api.operations.commands.status.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.StatusRepository;
import dev.farneser.tasktracker.api.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatchStatusCommandHandler implements CommandHandler<PatchStatusCommand, Void> {
    private final StatusRepository statusRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final Map<Long, Lock> userLocks = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public Void handle(PatchStatusCommand command) throws NotFoundException, OperationNotAuthorizedException {
        Long userId = command.getUserId();
        Lock userLock = userLocks.computeIfAbsent(userId, k -> new ReentrantLock());

        userLock.lock();

        try {
            ProjectMember member = projectMemberRepository
                    .findProjectMemberByProjectIdAndMemberId(command.getProjectId(), command.getUserId())
                    .orElseThrow(() -> new NotFoundException(""));

            if (!member.getRole().hasPermission(ProjectPermission.ADMIN_PATCH)) {
                throw new OperationNotAuthorizedException();
            }

            List<Status> columns = statusRepository.findByProjectIdOrderByOrderNumber(command.getProjectId()).orElse(new ArrayList<>());
            Status column = columns
                    .stream()
                    .filter(c -> c.getId().equals(command.getStatusId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Column with id " + command.getStatusId() + " not found"));

            if (command.getStatusName() != null) {
                column.setStatusName(command.getStatusName());
            }

            if (command.getOrderNumber() != null) {
                long oldOrder = column.getOrderNumber();
                long newOrder = command.getOrderNumber();

                column.setOrderNumber(newOrder);

                List<Status> columnsToChange = columns.stream().filter(c -> c.getOrderNumber() >= Math.min(oldOrder, newOrder) && c.getOrderNumber() <= Math.max(oldOrder, newOrder)).toList();

                OrderService.patchOrder(column.getId(), newOrder, oldOrder, columnsToChange);
            }

            if (command.getIsCompleted() != null) {
                column.setIsCompleted(command.getIsCompleted());
            }

            column.setEditDate(new Date(System.currentTimeMillis()));

            statusRepository.save(column);
        } finally {
            userLock.unlock();
        }

        return null;
    }
}
