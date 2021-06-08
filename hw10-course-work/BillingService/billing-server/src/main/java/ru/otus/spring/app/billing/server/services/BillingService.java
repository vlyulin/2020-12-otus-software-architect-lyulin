package ru.otus.spring.app.billing.server.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.app.billing.server.models.BillingOrder;

import java.util.List;

@Service
public interface BillingService {
    void doCommit(BillingOrder billingOrder);
    List<BillingOrder> getBillingOrdesByAccountId(int accountId);
}
