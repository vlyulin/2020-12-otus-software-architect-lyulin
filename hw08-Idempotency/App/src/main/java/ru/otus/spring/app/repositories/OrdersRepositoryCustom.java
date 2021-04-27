package ru.otus.spring.app.repositories;

//import org.springframework.data.repository.query.Param;
import ru.otus.spring.app.models.Order;

public interface OrdersRepositoryCustom {

//    @Modifying
//    @Query("update Customer u set u.phone = :phone where u.id = :id")
//    void updateOrder(@Param(value = "id") long id, @Param(value = "phone") String phone);

    Order safetySave(Order order);
}
