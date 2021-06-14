package ru.otus.spring.app.billing.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.spring.app.billing.client.dto.BillingOrderDTO;
import ru.otus.spring.app.twopc.common.models.TwoPCResource;
import ru.otus.spring.app.twopc.common.models.TwoPCState;

import java.util.logging.Logger;

public class BillingClient implements TwoPCResource {

    private Logger log = Logger.getLogger(BillingClient.class.getName());

    @Autowired
    private BillingFeignClient billingFeignClient;

//    private String baseBillingServerURL = "http://localhost:8082"; // /billing-service/
    private TwoPCState resourceState;
    private BillingOrderDTO billingOrderDTO;
//    private RestTemplate restTemplate;

    public BillingClient(BillingFeignClient billingFeignClient, BillingOrderDTO billingOrderDTO) {
        this.billingFeignClient = billingFeignClient;
        this.billingOrderDTO = billingOrderDTO;
    }

//    public BillingClient(RestTemplate restTemplate, BillingOrderDTO billingOrderDTO) {
//        this.billingOrderDTO = billingOrderDTO;
//        this.restTemplate = restTemplate;
//    }

//    @Override
//    public ResponseEntity<String> withdraw(int account_id, BillingOrderDTO billingOrderDTO) {
//        String serviceUrl = "http://localhost:8002/billing-service";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<BillingOrderDTO> request = new HttpEntity<>(billingOrderDTO, headers);
//        String billingResponse = restTemplate.postForObject(serviceUrl + "/accounts/"+account_id+"/withdraw", request, String.class);
//        return new ResponseEntity<String>(billingResponse, HttpStatus.OK);
//    }

    @Override
    public TwoPCState canCommit(int transactionId) {
        log.info("Enter.");
        if(billingFeignClient == null) {
            throw new RuntimeException("billingFeignClient is null.");
        }

        try {
            resourceState = billingFeignClient.canCommit(transactionId, billingOrderDTO);
            log.info("Get resourceState = "+resourceState.toString());
        }
        catch (Exception e) {
            log.info("billingFeignClient error: " + e.getMessage());
            throw e;
        }
        return resourceState;
//        if(restTemplate == null) {
//            throw new RuntimeException("canCommit: restTemplate is null");
//        }
//        String serviceUrl = baseBillingServerURL + "/canCommit/" + transactionId;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        Gson gson = new Gson();
//        String billingOrderDTOJson = gson.toJson(billingOrderDTO, BillingOrderDTO.class);
//        HttpEntity<String> request = new HttpEntity<>(billingOrderDTOJson, headers);
//        resourceState = restTemplate.postForObject(serviceUrl, request, TwoPCState.class);
    }

    @Override
    public TwoPCState preCommit(int transactionId) {
        log.info("Enter.");
        if(billingFeignClient == null) {
            throw new RuntimeException("billingFeignClient is null.");
        }

        try {
            resourceState = billingFeignClient.preCommit(transactionId);
            log.info("Get resourceState = "+resourceState.toString());
        }
        catch (Exception e) {
            log.info("billingFeignClient error: " + e.getMessage());
            throw e;
        }
        return resourceState;
//        try {
//            String serviceUrl = baseBillingServerURL + "/preCommit/" + transactionId;
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> request = new HttpEntity<>("null", headers);
//            resourceState = restTemplate.patchForObject(serviceUrl, request, TwoPCState.class);
//        } catch (Exception e) {
//            System.out.println("preCommit.error: " + e.getMessage());
//            throw e;
//        }
    }

    @Override
    public TwoPCState doCommit(int transactionId) {
        log.info("Enter.");
        if(billingFeignClient == null) {
            throw new RuntimeException("billingFeignClient is null.");
        }

        try {
            resourceState = billingFeignClient.doCommit(transactionId);
            log.info("Get resourceState = "+resourceState.toString());
        }
        catch (Exception e) {
            log.info("billingFeignClient error: " + e.getMessage());
            throw e;
        }
        return resourceState;
//        try {
//            String serviceUrl = baseBillingServerURL + "/doCommit/"+transactionId;
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> request = new HttpEntity<>("null", headers);
//            resourceState = restTemplate.patchForObject(serviceUrl, request, TwoPCState.class);
//        } catch (Exception e) {
//            System.out.println("doCommit.error: " + e.getMessage());
//            throw e;
//        }
    }

    @Override
    public TwoPCState abort(int transactionId) {
        log.info("Enter.");
        if(billingFeignClient == null) {
            throw new RuntimeException("billingFeignClient is null.");
        }

        try {
            resourceState = billingFeignClient.abort(transactionId);
            log.info("Get resourceState = "+resourceState.toString());
        }
        catch (Exception e) {
            log.info("billingFeignClient error: " + e.getMessage());
            throw e;
        }
        return resourceState;
//        try {
//            String serviceUrl = baseBillingServerURL + "/abort/"+transactionId;
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> request = new HttpEntity<>("null", headers);
//            resourceState = restTemplate.patchForObject(serviceUrl, request, TwoPCState.class);
//        } catch (Exception e) {
//            System.out.println("abort.error: " + e.getMessage());
//            throw e;
//        }
//        return resourceState;
    }

    @Override
    public TwoPCState getState() {
        return resourceState;
    }
}
