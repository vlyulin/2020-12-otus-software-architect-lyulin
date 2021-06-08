package ru.otus.spring.app.notification.client.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.spring.app.notification.client.dto.NotificationOrderDTO;
import ru.otus.spring.app.twopc.common.models.TwoPCResource;
import ru.otus.spring.app.twopc.common.models.TwoPCState;

import java.util.logging.Logger;

public class NotificationClient implements TwoPCResource {

    private Logger log = Logger.getLogger(NotificationClient.class.getName());

    @Autowired
    private NotificationFeignClient notificationFeignClient;

    private TwoPCState resourceState;
    private NotificationOrderDTO notificationOrderDTO;

    public NotificationClient(NotificationFeignClient notificationFeignClient, NotificationOrderDTO notificationOrderDTO) {
        this.notificationFeignClient = notificationFeignClient;
        this.notificationOrderDTO = notificationOrderDTO;
    }

    @Override
    public TwoPCState canCommit(int transactionId) {
        log.info("Enter.");
        if(notificationFeignClient == null) {
            throw new RuntimeException("notificationFeignClient is null.");
        }

        try {
            resourceState = notificationFeignClient.canCommit(transactionId, notificationOrderDTO);
        }
        catch (Exception e) {
            log.info("notificationFeignClient error: " + e.getMessage());
            throw e;
        }
        return resourceState;
    }

    @Override
    public TwoPCState preCommit(int transactionId) {
        log.info("Enter.");
        if(notificationFeignClient == null) {
            throw new RuntimeException("notificationFeignClient is null.");
        }

        try {
            resourceState = notificationFeignClient.preCommit(transactionId);
            log.info("Get resourceState = "+resourceState.toString());
        }
        catch (Exception e) {
            log.info("notificationFeignClient error: " + e.getMessage());
            throw e;
        }
        return resourceState;
    }

    @Override
    public TwoPCState doCommit(int transactionId) {
        log.info("Enter.");
        if(notificationFeignClient == null) {
            throw new RuntimeException("notificationFeignClient is null.");
        }

        try {
            resourceState = notificationFeignClient.doCommit(transactionId);
            log.info("Get resourceState = "+resourceState.toString());
        }
        catch (Exception e) {
            log.info("notificationFeignClient error: " + e.getMessage());
            throw e;
        }
        return resourceState;
    }

    @Override
    public TwoPCState abort(int transactionId) {
        log.info("Enter.");
        if(notificationFeignClient == null) {
            throw new RuntimeException("notificationFeignClient is null.");
        }

        try {
            resourceState = notificationFeignClient.abort(transactionId);
            log.info("Get resourceState = "+resourceState.toString());
        }
        catch (Exception e) {
            log.info("notificationFeignClient error: " + e.getMessage());
            throw e;
        }
        return resourceState;
    }

    @Override
    public TwoPCState getState() {
        return resourceState;
    }
}
