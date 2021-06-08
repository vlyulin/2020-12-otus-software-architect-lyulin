package ru.otus.spring.app.billing.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.app.billing.server.models.BillingOrder;
import ru.otus.spring.app.billing.server.repositories.BillingOrdersRepository;

import java.util.List;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingOrdersRepository billingOrdersRepository;

    @Override
    public void doCommit(BillingOrder billingOrder) {
        System.out.println("billingOrder = "+billingOrder.getAccountId());
        billingOrdersRepository.save(billingOrder);
    }

    @Override
    public List<BillingOrder> getBillingOrdesByAccountId(int accountId) {
        return billingOrdersRepository.findAllByAccountId(accountId);
    }
}
