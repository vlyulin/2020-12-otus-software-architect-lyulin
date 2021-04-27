package ru.otus.spring.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.app.models.Order;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long>, OrdersRepositoryCustom {
    boolean existsById(Long id);
    boolean existsByIdAndObjectVersionNumber(Long id, Integer objectVersionNumber);
}
