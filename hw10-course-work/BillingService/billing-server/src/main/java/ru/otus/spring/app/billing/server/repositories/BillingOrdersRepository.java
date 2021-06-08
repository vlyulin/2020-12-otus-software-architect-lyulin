package ru.otus.spring.app.billing.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.app.billing.server.models.BillingOrder;

import java.util.List;

@Repository
public interface BillingOrdersRepository extends JpaRepository<BillingOrder, Integer> {
    List<BillingOrder> findAllByAccountId(int accountId);
}
