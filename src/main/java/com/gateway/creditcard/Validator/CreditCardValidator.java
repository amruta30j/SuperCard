package com.gateway.creditcard.Validator;

import com.gateway.creditcard.controller.model.CreditCard;

import java.util.Objects;

/**
 * This abstract validator component provides
 * a skeleton implementation for Credit card object validation
 */
public abstract class CreditCardValidator {

    abstract boolean validateCardNumber(CreditCard creditCard);

    /** This method validates the card details for credit card name field
     * @param creditCard
     * @return
     */
    public boolean validateCardDetails(CreditCard creditCard) {
        return Objects.nonNull(creditCard.getName())
                && creditCard.getName().length() > 0
                && creditCard.getNumber().length()<=19;
    }

    /**
     * This method uses Template method design pattern
     *
     * @param creditCard
     * @return boolean
     */
    public boolean validate(CreditCard creditCard) {
        return validateCardDetails(creditCard) && validateCardNumber(creditCard);
    }
}
