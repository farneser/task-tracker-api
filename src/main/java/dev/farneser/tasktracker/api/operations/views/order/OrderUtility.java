package dev.farneser.tasktracker.api.operations.views.order;

import java.util.Comparator;
import java.util.List;

public abstract class OrderUtility {
    public static <T extends OrderIdentifier> void patchOrder(T element, Long targetOrder, List<T> order) {
        int minOrder = 1;
        int maxOrder = order.size();

        if (targetOrder < minOrder) {
            targetOrder = (long) minOrder;
        } else if (targetOrder > maxOrder) {
            targetOrder = (long) maxOrder;
        }

        order.sort(Comparator.comparing(T::getOrderNumber));

        order.remove(element);

        order.add(Math.toIntExact(targetOrder) - 1, element);

        for (int i = 0; i < order.size(); i++) {
            order.get(i).setOrderNumber((long) (i + 1));
        }
    }

    public static <T extends OrderIdentifier> void removeSpace(List<T> elements) {
        if (elements == null || elements.isEmpty()) {
            return;
        }

        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).setOrderNumber((long) (i + 1));
        }
    }
}
