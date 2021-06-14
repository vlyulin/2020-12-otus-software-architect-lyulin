package ru.otus.spring.app.notification.client.services;

import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.app.notification.client.dto.NotificationOrderDTO;
import ru.otus.spring.app.twopc.common.models.TwoPCState;

@FeignClient(name = "notification-service", url = "${notification-service-url:notification-service}")
// @FeignClient(name = "notification-service", url = "notification-service")
//@FeignClient(name = "notification-service", url = "localhost:8084")
public interface NotificationFeignClient {

//    @NewSpan
    @RequestMapping(method = RequestMethod.POST, value = "/canCommit/{transactionId}", consumes = "application/json")
    TwoPCState canCommit(@PathVariable("transactionId") int transactionID,
                         @RequestBody NotificationOrderDTO notificationOrderDTO);


//    @NewSpan
    @RequestMapping(method = RequestMethod.PUT, value = "/preCommit/{transactionId}")
    TwoPCState preCommit(@PathVariable("transactionId") int transactionID);

//    @NewSpan
    @RequestMapping(method = RequestMethod.PUT, value = "/doCommit/{transactionId}")
    TwoPCState doCommit(@PathVariable("transactionId") int transactionID);

//    @NewSpan
    @RequestMapping(method = RequestMethod.PUT, value = "/abort/{transactionId}")
    TwoPCState abort(@PathVariable("transactionId") int transactionID);
}
