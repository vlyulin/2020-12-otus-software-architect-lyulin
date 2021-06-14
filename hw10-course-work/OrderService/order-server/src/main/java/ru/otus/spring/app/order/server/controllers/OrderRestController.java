package ru.otus.spring.app.order.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.spring.app.order.server.models.Order;
import ru.otus.spring.app.order.server.services.OrdersService;

import javax.servlet.http.HttpServletResponse;

@RestController
public class OrderRestController {

    private OrdersService ordersService;

    public OrderRestController(@Autowired OrdersService ordersService) {
        this.ordersService = ordersService;
    }

//    @NewSpan
    @RequestMapping(value = "/orders/{orderid}", method = {RequestMethod.GET})
    @ResponseBody
    Order getOrder(@PathVariable(value = "orderId") Long orderId) {
        return ordersService.getOne(orderId);
    }

    // https://coderoad.ru/30558491/Spring-MVC-REST-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5-RequestBody-List-%D0%B2%D0%BE%D0%B7%D0%B2%D1%80%D0%B0%D1%89%D0%B0%D0%B5%D1%82-HTTP-400
//    @NewSpan
    @RequestMapping(value = "orders", headers = "X-UserId", method = {RequestMethod.POST})
    @ResponseBody
    ResponseEntity<String> placeOrder(
            @RequestBody Order order,
            @RequestHeader("X-UserId") String xUserdId,
            HttpServletResponse response,
            UriComponentsBuilder b)
    {
        int userId = Integer.valueOf(xUserdId);
        try {
            Order retOrder = ordersService.placeOrder(userId, order);
            UriComponents uriComponents =
                    b.path("/orders/{id}").buildAndExpand(retOrder.getId());
            ObjectMapper mapper = new ObjectMapper();
            return ResponseEntity.created(uriComponents.toUri()).body(mapper.writeValueAsString(retOrder));
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}
