package com.gateway.creditcard.controller;

import com.gateway.creditcard.controller.model.CreditCard;
import com.gateway.creditcard.service.CreditCardService;
import com.gateway.creditcard.util.response.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for Create/Get Credit card APIs
 */
@RestController
public class CreditCardController {

    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    /** This API method creates a Credit card and returns appropriate
     * HTTP Status response code upon creation
     * @param creditCard
     * @return ResponseEntity<String>
     */
    @PostMapping(value = "api/creditcard/v1/add")
    @ResponseBody
    public ResponseEntity<String> add(@RequestBody CreditCard creditCard) {
        ResponseEntity<String> response;
        ServiceResponse<CreditCard> serviceResponse = creditCardService.add(creditCard);
        if(serviceResponse.getServiceError()== null && serviceResponse.getResponse()!= null) {
            response = new ResponseEntity<>(HttpStatus.CREATED);
        } else if(serviceResponse.getServiceError()== ServiceResponse.ServiceError.VALIDATION_ERROR) {
            response = new ResponseEntity<>("Invalid card number", HttpStatus.BAD_REQUEST);
        } else if(serviceResponse.getServiceError()== ServiceResponse.ServiceError.ALREADY_EXISTS) {
            response = new ResponseEntity<>("Card already exists", HttpStatus.CONFLICT);
        } else {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    /** This API method returns a list of credit cards persisted
     * appropriate HTTP Status response code as well as
     * a list of CreditCards as a response body
     * @return ResponseEntity<List<CreditCard>>
     */
    @GetMapping(value = "api/creditcard/v1/get")
    @ResponseBody
    public ResponseEntity<List<CreditCard>> get() {
        ResponseEntity<List<CreditCard>> response;
        ServiceResponse<List<CreditCard>> serviceResponse = creditCardService.get();
        if(serviceResponse.getServiceError()== null && serviceResponse.getResponse()!= null) {
            response = new ResponseEntity<>(serviceResponse.getResponse(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
