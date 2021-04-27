package ru.otus.spring.app.services;

import ru.otus.spring.app.models.Order;

public interface OrdersService {
    Order placeOrder(Order order);
    Order getOne(Long orderId);
}
