package ru.otus.spring.app.billing.client.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.otus.spring.app.billing.client.dto.BillingOrderDTO;
import ru.otus.spring.app.twopc.common.models.TwoPCState;

@FeignClient(name = "billing-service", url = "${billing-service-url:billing-service}")
//@FeignClient(name = "billing-service", url = "localhost:8082")
public interface BillingFeignClient {

//    @NewSpan
    @RequestMapping(method = RequestMethod.POST, value = "/canCommit/{transactionId}", consumes = "application/json")
    TwoPCState canCommit(@PathVariable("transactionId") int transactionID,
                         @RequestBody BillingOrderDTO billingOrderDTO);

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
