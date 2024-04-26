package dev.farneser.tasktracker.api.service.order;

public interface OrderIdentifier {
    Long getId();

    Long getOrderNumber();

    void setOrderNumber(Long orderNumber);
}
