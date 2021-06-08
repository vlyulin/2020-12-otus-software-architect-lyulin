package ru.otus.spring.app.notification.server.services;

import ru.otus.spring.app.notification.client.dto.NotificationOrderDTO;
import ru.otus.spring.app.notification.server.models.NotificationOrder;
import ru.otus.spring.app.twopc.common.models.TwoPCResource;
import ru.otus.spring.app.twopc.common.models.TwoPCState;

public class NotificationServerResource implements TwoPCResource {

    private TwoPCState  state;
    private NotificationOrderDTO notificationOrderDTO;
    private NotificationService notificationService;

    public NotificationServerResource(NotificationOrderDTO notificationOrderDTO, NotificationService notificationService) {
        this.notificationOrderDTO = notificationOrderDTO;
        this.notificationService = notificationService;
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
        NotificationOrder notificationOrder = Utils.convert(notificationOrderDTO);
        if(notificationOrder == null) {
            throw new RuntimeException("NotificationOrderDTO is null");
        }
        notificationService.doCommit(notificationOrder);
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
