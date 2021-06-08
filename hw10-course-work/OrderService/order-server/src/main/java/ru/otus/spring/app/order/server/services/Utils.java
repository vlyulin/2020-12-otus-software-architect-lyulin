package ru.otus.spring.app.order.server.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.otus.spring.app.billing.client.dto.BillingOrderDTO;
import ru.otus.spring.app.billing.client.dto.CurrenciesEnum;
import ru.otus.spring.app.notification.client.dto.NotificationCommodityDTO;
import ru.otus.spring.app.notification.client.dto.NotificationOrderDTO;
import ru.otus.spring.app.order.server.models.Commodity;
import ru.otus.spring.app.order.server.models.Order;
import ru.otus.spring.app.order.server.models.OrderSatus;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Utils {
    public static BillingOrderDTO convert(int userId, Order order) {
        if(order == null) return null;

        BillingOrderDTO billingOrderDTO = new BillingOrderDTO();
        float totalSum = 0;
        for(Commodity item: order.getItems()) {
            totalSum += item.getPrice();
        }
        billingOrderDTO.setAmount(totalSum);
        billingOrderDTO.setCurrency(CurrenciesEnum.RUB);
        billingOrderDTO.setAccountId(userId);
        return billingOrderDTO;
    }

    public static NotificationOrderDTO convert(Order order) {
        if(order == null) return null;

        NotificationOrderDTO notificationOrderDTO = new NotificationOrderDTO();
        notificationOrderDTO.setId(order.getId());
        notificationOrderDTO.setObjectVersionNumber(order.getObjectVersionNumber());
        notificationOrderDTO.setIdempotencyKey(order.getIdempotencyKey());
        notificationOrderDTO.setCreatedBy(order.getCreatedBy());
        notificationOrderDTO.setShipDate(order.getShipDate());
        notificationOrderDTO.setComplete(order.isComplete());

        List<NotificationCommodityDTO> commodityDTOS = new ArrayList<>();
        for(Commodity commodity: order.getItems()) {
            NotificationCommodityDTO notificationCommodityDTO = new NotificationCommodityDTO();
            notificationCommodityDTO.setId(commodity.getId());
            notificationCommodityDTO.setQuantity(commodity.getQuantity());
            notificationCommodityDTO.setPrice(commodity.getPrice());
            commodityDTOS.add(notificationCommodityDTO);
        }
        notificationOrderDTO.setItems(commodityDTOS);
        return notificationOrderDTO;
    }
}
