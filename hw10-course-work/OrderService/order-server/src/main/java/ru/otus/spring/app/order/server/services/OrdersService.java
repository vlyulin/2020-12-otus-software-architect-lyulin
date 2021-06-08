package ru.otus.spring.app.order.server.services;

import ru.otus.spring.app.order.server.models.Order;

public interface OrdersService {
    Order placeOrder(int userId, Order order);
    Order getOne(Long orderId);
}
