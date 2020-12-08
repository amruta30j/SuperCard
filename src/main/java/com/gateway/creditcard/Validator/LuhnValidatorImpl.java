package com.gateway.creditcard.Validator;

import com.gateway.creditcard.controller.model.CreditCard;
import com.gateway.creditcard.util.LuhnCheckDigit;
import org.springframework.stereotype.Component;

/** This component extends the CreditCardValidator and provides
 * an implementation using LuhnCheckDigit check. This way we can
 * have multiple components extending from the same class doing different Validations
 * based on different validation mechanisms
 */
@Component("luhnValidatorImpl")
public class LuhnValidatorImpl extends CreditCardValidator {

    private final LuhnCheckDigit luhnCheckDigit;

    public LuhnValidatorImpl(LuhnCheckDigit luhnCheckDigit) {
        this.luhnCheckDigit = luhnCheckDigit;
    }

    @Override
    public boolean validateCardNumber(CreditCard creditCard) {
        String creditCardNumber = creditCard.getNumber();
        return luhnCheckDigit.checkDigit(creditCardNumber);
    }
}
