package ru.otus.spring.app.notification.server.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.app.notification.server.models.NotificationOrder;

import java.util.List;

//@Service
public interface NotificationService {
    void doCommit(NotificationOrder notificationOrder);
    List<NotificationOrder> getNotificationOrdersByCreatedBy(Long createdBy);
}
