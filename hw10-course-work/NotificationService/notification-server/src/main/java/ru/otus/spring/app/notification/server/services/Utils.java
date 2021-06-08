package ru.otus.spring.app.notification.server.services;

import ru.otus.spring.app.notification.client.dto.NotificationCommodityDTO;
import ru.otus.spring.app.notification.client.dto.NotificationOrderDTO;
import ru.otus.spring.app.notification.server.models.NotificationCommodity;
import ru.otus.spring.app.notification.server.models.NotificationOrder;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static NotificationOrder convert(NotificationOrderDTO notificationOrderDTO) {
        if(notificationOrderDTO == null) return null;

        NotificationOrder notificationOrder = new NotificationOrder();
        notificationOrder.setId(notificationOrderDTO.getId());
        notificationOrder.setObjectVersionNumber(notificationOrderDTO.getObjectVersionNumber());
        notificationOrder.setIdempotencyKey(notificationOrderDTO.getIdempotencyKey());
        notificationOrder.setCreatedBy(notificationOrderDTO.getCreatedBy());
        notificationOrder.setShipDate(notificationOrderDTO.getShipDate());
        notificationOrder.setComplete(notificationOrderDTO.isComplete());

        List<NotificationCommodity> commodities = getNotificationCommodity(notificationOrder, notificationOrderDTO);
        notificationOrder.setItems(commodities);

        return notificationOrder;
    }

    private static List<NotificationCommodity> getNotificationCommodity(
            NotificationOrder notificationOrder,
            NotificationOrderDTO notificationOrderDTO)
    {
        List<NotificationCommodity> commodities = new ArrayList<>();
        for(NotificationCommodityDTO commodityDTO: notificationOrderDTO.getItems()) {
            NotificationCommodity notificationCommodity = new NotificationCommodity();
            notificationCommodity.setId(commodityDTO.getId());
            notificationCommodity.setOrder(notificationOrder);
            notificationCommodity.setPrice(commodityDTO.getPrice());
            notificationCommodity.setQuantity(commodityDTO.getQuantity());
            commodities.add(notificationCommodity);
        }
        return commodities;
    }
}
