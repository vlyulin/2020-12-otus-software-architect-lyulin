package ru.otus.spring.app.billing.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.spring.app.billing.client.dto.BillingOrderDTO;
import ru.otus.spring.app.billing.server.models.BillingOrder;
import ru.otus.spring.app.billing.server.repositories.BillingOrdersRepository;
import ru.otus.spring.app.billing.server.services.BillingServerResource;
import ru.otus.spring.app.billing.server.services.BillingService;
import ru.otus.spring.app.twopc.common.models.TwoPCState;
import ru.otus.spring.app.twopc.common.models.TwoPCTransactionImpl;
import ru.otus.spring.app.twopc.common.services.TwoPCTransactionManagerService;
import ru.otus.spring.app.twopc.common.exceptions.TransactionDoesNotExists;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class BillingServiceController {

    private Logger log = Logger.getLogger(BillingServiceController.class.getName());

    @Autowired
    private BillingService billingService;
    @Autowired
    private TwoPCTransactionManagerService twoPCTransactionManagerService;

    @RequestMapping(value = "/canCommit/{transactionId}", method = {RequestMethod.POST})
    @ResponseBody
    TwoPCState canCommit(@PathVariable("transactionId") int transactionID,
                         @RequestBody BillingOrderDTO billingOrderDTO) {
        try {
            log.info("In the canCommit");
            BillingServerResource billingServerResource =
                    new BillingServerResource(billingOrderDTO, billingService);
            TwoPCTransactionImpl twoPCTransaction = new TwoPCTransactionImpl();
            twoPCTransaction.add(billingServerResource);
            twoPCTransaction.setTransactionState(TwoPCState.WORKING);
            twoPCTransaction.setTransactionId(transactionID); // twoPCTransactionManagerService.getNextTransactionId());
            twoPCTransactionManagerService.addTransaction(twoPCTransaction);
            return twoPCTransactionManagerService.canCommit(transactionID);
        } catch (TransactionDoesNotExists e) {
            // https://www.baeldung.com/exception-handling-for-rest-with-spring
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/preCommit/{transactionId}", method = {RequestMethod.PUT})
    @ResponseBody
    public TwoPCState preCommit(@PathVariable("transactionId") int transactionID) {
        try {
            return twoPCTransactionManagerService.preCommit(transactionID);
        } catch (TransactionDoesNotExists e) {
            // https://www.baeldung.com/exception-handling-for-rest-with-spring
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/doCommit/{transactionId}", method = {RequestMethod.PUT})
    @ResponseBody
    public TwoPCState doCommit(@PathVariable("transactionId") int transactionID) {
        try {
            return twoPCTransactionManagerService.doCommit(transactionID);
        } catch (TransactionDoesNotExists e) {
            // https://www.baeldung.com/exception-handling-for-rest-with-spring
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/abort/{transactionId}", method = {RequestMethod.PUT})
    @ResponseBody
    public TwoPCState abort(@PathVariable("transactionId") int transactionID) {
        try {
            return twoPCTransactionManagerService.abort(transactionID);
        } catch (TransactionDoesNotExists e) {
            // https://www.baeldung.com/exception-handling-for-rest-with-spring
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/accounts/billingOrders/{account_id}", method = {RequestMethod.GET})
    @ResponseBody
    public List<BillingOrder> getBillingOrdersByAccountId(@PathVariable("account_id") int accountId) {
        try {
            return billingService.getBillingOrdesByAccountId(accountId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }
}
