package ru.otus.spring.app.notification.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.app.notification.server.models.NotificationOrder;
import ru.otus.spring.app.notification.server.repositories.NotificationOrdersRepository;

import java.util.List;
import java.util.logging.Logger;

@Service
public class NotificationServiceImpl implements NotificationService {

    private Logger log = Logger.getLogger(NotificationServiceImpl.class.getName());

    @Autowired
    private NotificationOrdersRepository notificationOrdersRepository;

    @Override
    public void doCommit(NotificationOrder notificationOrder) {
        notificationOrdersRepository.save(notificationOrder);
    }

    @Override
    public List<NotificationOrder> getNotificationOrdersByCreatedBy(Long createdBy) {
        return notificationOrdersRepository.findAllByCreatedBy(createdBy);
    }
}
