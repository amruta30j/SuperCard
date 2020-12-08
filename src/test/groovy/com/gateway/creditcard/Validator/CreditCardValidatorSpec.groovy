package com.gateway.creditcard.Validator

import com.gateway.creditcard.controller.model.CreditCard
import com.gateway.creditcard.util.LuhnCheckDigit
import spock.lang.Specification

class CreditCardValidatorSpec extends Specification {

    def luhnCheckDigitMock = Mock(LuhnCheckDigit)
    def validCreditCardMock = Mock(CreditCard)
    def invalidCreditCardMock = Mock(CreditCard)

    def setup() {
        validCreditCardMock.name >> 'SomeCreditCardHolderName'
        validCreditCardMock.number >> '4444333322221111'

        invalidCreditCardMock.name >> null
        invalidCreditCardMock.number >> '4444333322221111'
    }

    def 'Should validate the credit card request object and return TRUE'() {

        given: 'a test validator object'
        def testObj = new LuhnValidatorImpl(luhnCheckDigitMock)

        and: 'a valid credit card request payload and the luhn check is also successful'
        validCreditCardMock.name >> 'SomeCreditCardHolderName'
        validCreditCardMock.number >> '4444333322221111'

        luhnCheckDigitMock.checkDigit(validCreditCardMock.number) >> true

        when: 'invoke the validate method'
        def result = testObj.validate(validCreditCardMock)

        then:
        result == true
    }

    def 'Should validate the credit card number length object and return FALSE'() {

        given: 'a test validator object'
        def testObj = new LuhnValidatorImpl(luhnCheckDigitMock)

        and: 'invalid request credit card payload containing MORE THAN 19 DIGITS and the luhn check is also successful'
        invalidCreditCardMock.name >> 'someValidName'
        invalidCreditCardMock.number >> '44443333222211111154'

        luhnCheckDigitMock.checkDigit(validCreditCardMock.number) >> true

        when: 'invoke the validate method'
        def result = testObj.validate(invalidCreditCardMock)

        then:
        result == false
    }
    
    def 'Should validate the credit card request object and return FALSE'() {

        given: 'a test validator object'
        def testObj = new LuhnValidatorImpl(luhnCheckDigitMock)

        and: 'a valid credit card request payload and the luhn check is also successful'
        invalidCreditCardMock.name >> null
        invalidCreditCardMock.number >> '4444333322221111'

        luhnCheckDigitMock.checkDigit(validCreditCardMock.number) >> true

        when: 'invoke the validate method'
        def result = testObj.validate(invalidCreditCardMock)

        then:
        result == false
    }

    def 'Should validate the credit card request object and return FALSE when both name and luhn digit check is FALSE'() {

        given: 'a test validator object'
        def testObj = new LuhnValidatorImpl(luhnCheckDigitMock)

        and: 'a valid credit card request payload and the luhn check is also successful'
        invalidCreditCardMock.name >> ''
        invalidCreditCardMock.number >> '4444333322221111'

        luhnCheckDigitMock.checkDigit(validCreditCardMock.number) >> false

        when: 'invoke the validate method'
        def result = testObj.validate(invalidCreditCardMock)

        then:
        result == false
    }
}
