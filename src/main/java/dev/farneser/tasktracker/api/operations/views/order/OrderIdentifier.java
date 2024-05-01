package dev.farneser.tasktracker.api.operations.views.order;

public interface OrderIdentifier {
    Long getId();

    Long getOrderNumber();

    void setOrderNumber(Long orderNumber);
}
