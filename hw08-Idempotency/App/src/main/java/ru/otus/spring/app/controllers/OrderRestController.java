package ru.otus.spring.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.spring.app.models.Order;
import ru.otus.spring.app.services.OrdersService;

import javax.servlet.http.HttpServletResponse;

@RestController
public class OrderRestController {

    private OrdersService ordersService;

    public OrderRestController(@Autowired OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @RequestMapping(value = "/orders/{orderid}", method = {RequestMethod.GET})
    @ResponseBody
    Order getOrder(@PathVariable(value = "orderId") Long orderId) {
        return ordersService.getOne(orderId);
    }

    @RequestMapping(value = "orders", method = {RequestMethod.POST})
    @ResponseBody
    ResponseEntity<String> placeOrder(@RequestBody Order order, HttpServletResponse response, UriComponentsBuilder b)
    {
        try {
            Order retOrder = ordersService.placeOrder(order);
            UriComponents uriComponents =
                    b.path("/orders/{id}").buildAndExpand(retOrder.getId());
            ObjectMapper mapper = new ObjectMapper();
            return ResponseEntity.created(uriComponents.toUri()).body(mapper.writeValueAsString(retOrder));
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}
