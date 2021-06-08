package ru.otus.spring.app.billing.server.services;

import ru.otus.spring.app.billing.client.dto.BillingOrderDTO;
import ru.otus.spring.app.billing.server.models.BillingOrder;
import ru.otus.spring.app.twopc.common.models.TwoPCResource;
import ru.otus.spring.app.twopc.common.models.TwoPCState;

public class BillingServerResource implements TwoPCResource {

    private TwoPCState  state;
    private BillingOrderDTO billingOrderDTO;
    private BillingService billingService;

    public BillingServerResource(BillingOrderDTO billingOrderDTO, BillingService billingService) {
        this.billingOrderDTO = billingOrderDTO;
        this.billingService = billingService;
    }

    @Override
    public TwoPCState canCommit(int transactionId) {
        state = TwoPCState.WORKING;
        return state;
    }

    @Override
    public TwoPCState preCommit(int transactionId) {
        state = TwoPCState.PREPARED;
        return state;
    }

    @Override
    public TwoPCState doCommit(int transactionId) {
        BillingOrder billingOrder = Utils.convert(billingOrderDTO);
        if(billingOrder == null) {
            throw new RuntimeException("BillingOrderDTO is null");
        }
        billingService.doCommit(billingOrder);
        state = TwoPCState.COMMITTED;
        return state;
    }

    @Override
    public TwoPCState abort(int transactionId) {
        state = TwoPCState.ABORTED;
        return state;
    }

    @Override
    public TwoPCState getState() {
        return state;
    }
}
