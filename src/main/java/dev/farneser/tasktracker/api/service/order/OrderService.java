package dev.farneser.tasktracker.api.service.order;

import dev.farneser.tasktracker.api.models.KanbanTask;

import java.util.List;

public abstract class OrderService {
    public static void patchOrder(Long objectId, Long orderNumber, Long oldOrder, List<? extends OrderIdentifier> order) {
        order.forEach(e -> {
            if (e.getId().equals(objectId)) {
                return;
            }

            if (oldOrder < orderNumber) {
                e.setOrderNumber(e.getOrderNumber() - 1);
            } else {
                e.setOrderNumber(e.getOrderNumber() + 1);
            }
        });
    }
}
