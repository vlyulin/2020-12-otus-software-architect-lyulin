package ru.otus.spring.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.app.models.Order;
import ru.otus.spring.app.repositories.OrdersRepository;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    OrdersRepository ordersRepository;

    @Override
    public Order placeOrder(Order order) {

        if (order == null) {
            throw new RuntimeException("Order is null");
        }
        return ordersRepository.safetySave(order);
    }

    @Override
    public Order getOne(Long orderId) {
        return ordersRepository.getOne(orderId);
    }
}
