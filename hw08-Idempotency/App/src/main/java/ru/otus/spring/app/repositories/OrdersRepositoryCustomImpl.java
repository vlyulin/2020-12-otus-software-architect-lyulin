package ru.otus.spring.app.repositories;

import ru.otus.spring.app.models.Commodity;
import ru.otus.spring.app.models.Order;
import ru.otus.spring.app.repositories.exceptions.VersionsIsNotMatchException;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

public class OrdersRepositoryCustomImpl implements OrdersRepositoryCustom {

    public static final String VERSIONS_IS_NOT_MATCH = "Versions is not match";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Order safetySave(Order order) {
        if (order.getId() != null && order.getId() != 0) {
            Order persistedOrder = entityManager.find(Order.class, order.getId(), LockModeType.PESSIMISTIC_WRITE);
            if (persistedOrder.getObjectVersionNumber() != order.getObjectVersionNumber()) {
                throw new VersionsIsNotMatchException(VERSIONS_IS_NOT_MATCH);
            }
            persistedOrder.setObjectVersionNumber(order.getObjectVersionNumber() + 1);
            List<Commodity> order_items = order.getItems();
            for(Commodity item: order_items) {
                item.setOrder(persistedOrder);
            }
            List<Commodity> items = persistedOrder.getItems();
            items.clear();
            items.addAll(order.getItems());
            persistedOrder.setIdempotencyKey(order.getIdempotencyKey());
            persistedOrder.setStatus(order.getStatus());
            persistedOrder.setShipDate(order.getShipDate());
            entityManager.merge(persistedOrder); // .persist(persistedOrder);
        } else {
            List<Commodity> items = order.getItems();
            for (Commodity item : items) {
                item.setOrder(order);
            }
            entityManager.persist(order);
        }
        return order;
    }
}
