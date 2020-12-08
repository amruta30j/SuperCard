package com.gateway.creditcard.service;

import com.gateway.creditcard.Validator.CreditCardValidator;
import com.gateway.creditcard.controller.model.CreditCard;
import com.gateway.creditcard.repository.CreditCardRepository;
import com.gateway.creditcard.repository.model.CreditCardEntity;
import com.gateway.creditcard.util.response.ServiceResponse;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditCardService {

    private final CreditCardValidator creditCardValidator;
    private final CreditCardRepository creditCardRepository;

    //Constructor for CreditCardService
    public CreditCardService(CreditCardValidator creditCardValidator, CreditCardRepository creditCardRepository) {
        this.creditCardValidator = creditCardValidator;
        this.creditCardRepository = creditCardRepository;
    }

    /**
     * This method adds the new credit card to the repository
     *
     * @param creditCard
     * @return
     */
    public ServiceResponse<CreditCard> add(CreditCard creditCard) {
        ServiceResponse<CreditCard> response = new ServiceResponse<>();
        try {
            //This block is validating the credit card request object
            if (creditCardValidator.validate(creditCard)) {
                //When validated successfully, it then transforms it to an CreditCardEntity type
                CreditCardEntity creditCardEntity = transform(creditCard);
                //This block is ensuring that there is no already existing credit card
                if (creditCardRepository.findById(Long.valueOf(creditCardEntity.getCardNumber())).isPresent()) {
                    //if the card exists already, a service error will be set with an appropriate error message
                    response.setServiceError(ServiceResponse.ServiceError.ALREADY_EXISTS);
                } else {
                    //Credit card persisting is taking place here
                    creditCardRepository.save(creditCardEntity);
                    response.setResponse(creditCard);
                    response.setServiceError(null);
                }
            } else {
                //The credit card request is not validated due to bad data, setting an appropriate error
                response.setServiceError(ServiceResponse.ServiceError.VALIDATION_ERROR);
            }
            //When any unchecked database exception encounters, lets catch it and set an appropriate service error
        } catch (PersistenceException ex) {
            response.setServiceError(ServiceResponse.ServiceError.DATABASE_ERROR);
        }
        return response;
    }

    /**
     * This method get a list of CreditCards and
     * set them as a response in the ServiceResponse Object after
     * transforming creditCardEntity to CreditCard model Object
     *
     * @return
     */
    public ServiceResponse<List<CreditCard>> get() {
        ServiceResponse<List<CreditCard>> response = new ServiceResponse<>();
        try {
            //This block of code fetches all the credit cards in the form of list
            //Streaming over the list to map and transform it to CreditCard model object
            //Using stream leads to more clean and declarative way of iterating over the collection
            response.setResponse(creditCardRepository.findAll()
                    .stream()
                    .map(creditCardEntity ->
                            new CreditCard(creditCardEntity.getCardHolderName(),
                                    creditCardEntity.getCardNumber(),
                                    creditCardEntity.getCardBalance())).collect(Collectors.toList()));
        } catch (PersistenceException ex) {
            //Just to not terminate the program in the middle catching this unchecked exception and
            //setting an appropriate error message in the ServiceResponse
            response.setServiceError(ServiceResponse.ServiceError.DATABASE_ERROR);
        }
        return response;
    }

    /**
     * This method transforms a CreditCard model Object
     * to CreditCardEntity type to persist.
     *
     * @param creditCard
     * @return CreditCardEntity
     */
    private CreditCardEntity transform(CreditCard creditCard) {
        CreditCardEntity creditCardEntity = new CreditCardEntity();
        creditCardEntity.setCardHolderName(creditCard.getName());
        creditCardEntity.setCardNumber(creditCard.getNumber());
        creditCardEntity.setCardBalance(0.0d);
        return creditCardEntity;
    }
}
