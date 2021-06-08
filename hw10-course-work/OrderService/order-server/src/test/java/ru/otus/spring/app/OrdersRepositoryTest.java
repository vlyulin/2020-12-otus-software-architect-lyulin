package ru.otus.spring.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.app.order.server.models.Commodity;
import ru.otus.spring.app.order.server.models.Order;
import ru.otus.spring.app.order.server.models.OrderSatus;
import ru.otus.spring.app.order.server.repositories.OrdersRepository;
import ru.otus.spring.app.order.server.repositories.exceptions.VersionsIsNotMatchException;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@DisplayName("Тестирование репозитория OrderRepository")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrdersRepositoryTest {

    public static final long CREATED_BY = 101L;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private TestEntityManager entityManager;

    Order getNewOrder(Long userId) {
        Random rand = new Random();
        Commodity commodity1 = new Commodity();
        commodity1.setPrice(Math.round(rand.nextFloat() * 100 * 100) / 100);
        commodity1.setQuantity(1);
        Commodity commodity2 = new Commodity();
        commodity2.setPrice(Math.round(rand.nextFloat() * 100 * 100) / 100);
        commodity2.setQuantity(2);
        List<Commodity> commodityList = Arrays.asList(commodity1, commodity2);

        UUID uuid = UUID.randomUUID();

        Order order = new Order();
        order.setObjectVersionNumber(1);
        commodity1.setOrder(order);
        commodity2.setOrder(order);
        order.setIdempotencyKey(uuid);
        order.setStatus(OrderSatus.PLACED);
        order.setCreatedBy(CREATED_BY);
        order.getItems().add(commodity1);
        order.getItems().add(commodity2);
        return order;
    }

    @Test
    @DisplayName("Проверка ключа индемпотентности при создании order")
    public void createOrder() {

        Commodity commodity1 = new Commodity();
        commodity1.setPrice(99.99f);
        commodity1.setQuantity(1);
        Commodity commodity2 = new Commodity();
        commodity2.setPrice(54.32f);
        commodity2.setQuantity(2);
        List<Commodity> commodityList = Arrays.asList(commodity1, commodity2);

        UUID uuid = UUID.randomUUID();

        Order order = new Order();
        order.setObjectVersionNumber(1);
        commodity1.setOrder(order);
        commodity2.setOrder(order);
        order.setIdempotencyKey(uuid);
        order.setStatus(OrderSatus.PLACED);
        order.setCreatedBy(CREATED_BY);
        order.getItems().add(commodity1);
        order.getItems().add(commodity2);
        order = ordersRepository.safetySave(order);

        System.out.println("order.id = " + order.getId());
        order = ordersRepository.getOne(order.getId());

        Commodity commodity21 = new Commodity();
        commodity1.setPrice(77.99f);
        commodity1.setQuantity(1);
        Commodity commodity22 = new Commodity();
        commodity2.setPrice(36.32f);
        commodity2.setQuantity(2);
        List<Commodity> commodityList2 = Arrays.asList(commodity21, commodity22);

        Order order2 = new Order();
        order2.setObjectVersionNumber(1);
        order2.setIdempotencyKey(uuid);
        order2.setStatus(OrderSatus.PLACED);
        order2.setCreatedBy(CREATED_BY);
        order2.getItems().add(commodity1);
        order2.getItems().add(commodity2);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            ordersRepository.safetySave(order2);
            entityManager.flush();
        });
    }

    @Test
    @DisplayName("Проверка версионности при сохранении order")
    public void updateOrder() {

        Order order = getNewOrder(101L);

        // Сохранение 1 версии 1 сессией
        order = ordersRepository.safetySave(order);

        // Сохранение 2 версии 1 сессией
        order.setIdempotencyKey(UUID.randomUUID());
        ordersRepository.safetySave(order);

        Order o = ordersRepository.getOne(order.getId());
        entityManager.detach(order);

        // Сохранение 1 версии 2 сессией
        order.setObjectVersionNumber(1);
        order.setIdempotencyKey(UUID.randomUUID());

        Order[] arr = new Order[1];
        arr[0] = order;
        Assertions.assertThrows(VersionsIsNotMatchException.class, () -> {
            ordersRepository.safetySave(arr[0]);
        });
    }
}