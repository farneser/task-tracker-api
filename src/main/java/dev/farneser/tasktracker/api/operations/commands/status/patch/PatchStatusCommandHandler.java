package dev.farneser.tasktracker.api.operations.commands.status.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.StatusRepository;
import dev.farneser.tasktracker.api.service.order.OrderUtility;
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

    private void patchOrder(Long orderNumber, Status status, List<Status> statuses) {
        if (orderNumber != null) {
            long oldOrder = status.getOrderNumber();
            long newOrder = orderNumber;

            status.setOrderNumber(newOrder);

            List<Status> statusesToChange = statuses.stream()
                    .filter(c -> c.getOrderNumber() >= Math.min(oldOrder, newOrder) && c.getOrderNumber() <= Math.max(oldOrder, newOrder))
                    .toList();

            OrderUtility.patchOrder(status.getId(), newOrder, oldOrder, statusesToChange);
        }
    }

    @Override
    @Transactional
    public Void handle(PatchStatusCommand command) throws NotFoundException, OperationNotAuthorizedException {
        Long userId = command.getUserId();
        Lock userLock = userLocks.computeIfAbsent(userId, k -> new ReentrantLock());

        userLock.lock();

        try {
            Status status = statusRepository.findById(command.getStatusId()).orElseThrow(() -> new NotFoundException(""));

            ProjectMember member = projectMemberRepository
                    .findProjectMemberByProjectIdAndMemberId(status.getProject().getId(), command.getUserId())
                    .orElseThrow(() -> new NotFoundException(""));

            if (!member.getRole().hasPermission(ProjectPermission.ADMIN_PATCH)) {
                throw new OperationNotAuthorizedException();
            }

            List<Status> statuses = statusRepository
                    .findByProjectIdOrderByOrderNumber(member.getProject().getId())
                    .orElse(new ArrayList<>());

            if (command.getStatusName() != null) {
                status.setStatusName(command.getStatusName());
            }

            patchOrder(command.getOrderNumber(), status, statuses);

            if (command.getStatusColor() != null) {
                status.setStatusColor(command.getStatusColor());
            }

            if (command.getIsCompleted() != null) {
                status.setIsCompleted(command.getIsCompleted());
            }

            status.setEditDate(new Date(System.currentTimeMillis()));

            statusRepository.save(status);
        } finally {
            userLock.unlock();
        }

        return null;
    }
}
