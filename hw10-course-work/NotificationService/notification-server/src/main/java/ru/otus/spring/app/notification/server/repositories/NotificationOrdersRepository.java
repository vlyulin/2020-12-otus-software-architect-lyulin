package ru.otus.spring.app.notification.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.app.notification.server.models.NotificationOrder;

import java.util.List;

@Repository
public interface NotificationOrdersRepository extends JpaRepository<NotificationOrder, Integer> {
    List<NotificationOrder> findAllByCreatedBy(Long createdBy);
}
