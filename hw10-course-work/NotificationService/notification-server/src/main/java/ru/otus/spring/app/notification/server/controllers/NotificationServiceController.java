package ru.otus.spring.app.notification.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ru.otus.spring.app.notification.client.dto.NotificationOrderDTO;
import ru.otus.spring.app.notification.server.models.NotificationOrder;
import ru.otus.spring.app.notification.server.services.NotificationServerResource;
import ru.otus.spring.app.notification.server.services.NotificationService;
import ru.otus.spring.app.twopc.common.models.TwoPCState;
import ru.otus.spring.app.twopc.common.models.TwoPCTransactionImpl;
import ru.otus.spring.app.twopc.common.services.TwoPCTransactionManagerService;
import ru.otus.spring.app.twopc.common.exceptions.TransactionDoesNotExists;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class NotificationServiceController {

    private Logger log = Logger.getLogger(NotificationServiceController.class.getName());

    @Autowired
    private NotificationService NotificationService;
    @Autowired
    private TwoPCTransactionManagerService twoPCTransactionManagerService;

//    @NewSpan
    @RequestMapping(value = "/canCommit/{transactionId}", method = {RequestMethod.POST})
    @ResponseBody
    TwoPCState canCommit(@PathVariable("transactionId") int transactionID,
                         @RequestBody NotificationOrderDTO notificationOrderDTO) {
        try {
            log.info("In the canCommit");
            NotificationServerResource notificationServerResource =
                    new NotificationServerResource(notificationOrderDTO, NotificationService);
            TwoPCTransactionImpl twoPCTransaction = new TwoPCTransactionImpl();
            twoPCTransaction.add(notificationServerResource);
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

//    @NewSpan
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

//    @NewSpan
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

//    @NewSpan
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

//    @NewSpan
    @RequestMapping(value = "/accounts/notificationOrders/{account_id}", method = {RequestMethod.GET})
    @ResponseBody
    public List<NotificationOrder> getNotificationOrdersByAccountId(@PathVariable("account_id") Long accountId) {
        try {
            return NotificationService.getNotificationOrdersByCreatedBy(accountId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }
}
