package ru.otus.spring.app.order.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.app.billing.client.dto.BillingOrderDTO;
import ru.otus.spring.app.billing.client.service.BillingClient;
import ru.otus.spring.app.billing.client.service.BillingFeignClient;
import ru.otus.spring.app.notification.client.dto.NotificationOrderDTO;
import ru.otus.spring.app.notification.client.services.NotificationClient;
import ru.otus.spring.app.notification.client.services.NotificationFeignClient;
import ru.otus.spring.app.order.server.models.Order;
import ru.otus.spring.app.order.server.repositories.OrdersRepository;
import ru.otus.spring.app.twopc.common.models.TwoPCTransactionImpl;
import ru.otus.spring.app.twopc.common.services.TwoPCTransactionManagerService;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private TwoPCTransactionManagerService twoPCTransactionManagerService;

    @Autowired
    private BillingFeignClient billingFeignClient;

    @Autowired
    private NotificationFeignClient notificationFeignClient;

    @Override
    public Order placeOrder(int userId, Order order) {

        if (order == null) {
            throw new RuntimeException("Order is null");
        }
        Order savedOrder = ordersRepository.safetySave(order);

        BillingOrderDTO billingOrderDTO = Utils.convert(userId, order);
        BillingClient billingClient = new BillingClient(billingFeignClient, billingOrderDTO);

        NotificationOrderDTO notificationOrderDTO = Utils.convert(order);
        NotificationClient notificationClient = new NotificationClient(notificationFeignClient, notificationOrderDTO);

        TwoPCTransactionImpl twoPCTransactionImpl = new TwoPCTransactionImpl();
        int trxId = twoPCTransactionManagerService.getNextTransactionId();
        twoPCTransactionImpl.setTransactionId(trxId);
        twoPCTransactionImpl.add(billingClient);
        twoPCTransactionImpl.add(notificationClient);
        twoPCTransactionManagerService.addTransaction(twoPCTransactionImpl);
        twoPCTransactionManagerService.process(twoPCTransactionImpl.getTransactionId());

        return savedOrder;
    }

    @Override
    public Order getOne(Long orderId) {
        return ordersRepository.getOne(orderId);
    }
}
