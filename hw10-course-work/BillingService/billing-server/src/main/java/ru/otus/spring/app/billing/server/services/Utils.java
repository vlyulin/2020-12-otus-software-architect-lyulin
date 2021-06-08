package ru.otus.spring.app.billing.server.services;

import ru.otus.spring.app.billing.client.dto.BillingOrderDTO;
import ru.otus.spring.app.billing.server.models.BillingOrder;

public class Utils {
    public static BillingOrder convert(BillingOrderDTO billingOrderDTO) {
        if(billingOrderDTO == null) return null;

        BillingOrder billingOrder = new BillingOrder();
        billingOrder.setAccountId(billingOrderDTO.getAccountId());
        billingOrder.setAmount(billingOrderDTO.getAmount());
        billingOrder.setCurrency(billingOrderDTO.getCurrency());
        return billingOrder;
    }
}
