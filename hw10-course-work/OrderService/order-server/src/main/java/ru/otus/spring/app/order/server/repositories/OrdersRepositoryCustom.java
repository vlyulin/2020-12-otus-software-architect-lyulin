package ru.otus.spring.app.order.server.repositories;

import ru.otus.spring.app.order.server.models.Order;

public interface OrdersRepositoryCustom {
    Order safetySave(Order order);
}
