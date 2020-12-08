package com.gateway.creditcard.service

import com.gateway.creditcard.Validator.CreditCardValidator
import com.gateway.creditcard.controller.model.CreditCard
import com.gateway.creditcard.repository.CreditCardRepository
import com.gateway.creditcard.repository.model.CreditCardEntity
import com.gateway.creditcard.util.response.ServiceResponse
import spock.lang.Specification

import javax.persistence.PersistenceException

class CreditCardServiceSpec extends Specification {

    def creditCardValidatorMock = Mock(CreditCardValidator)
    def creditCardRepositoryMock = Mock(CreditCardRepository)

    def creditCardMock = Mock(CreditCard)

    def creditCardEntityMock1 = Mock(CreditCardEntity)
    def creditCardEntityMock2 = Mock(CreditCardEntity)
    def creditCardEntityMock = Mock(CreditCardEntity)

    def setup() {
        creditCardMock.name >> 'SomeCreditCardHolderName'
        creditCardMock.number >> '4444333322221111'
        creditCardMock.limit >> 0

        creditCardEntityMock1.cardHolderName >> 'SomeCreditCardHolderName'
        creditCardEntityMock1.cardNumber >> '4444333322221111'
        creditCardEntityMock1.cardBalance >> 0

        creditCardEntityMock2.cardHolderName >> 'SomeCreditCardHolderName'
        creditCardEntityMock2.cardNumber >> '4444333322221111'
        creditCardEntityMock2.cardBalance >> 0

        creditCardEntityMock.cardHolderName >> 'SomeCreditCardHolderName'
        creditCardEntityMock.cardNumber >> '4444333322221111'
        creditCardEntityMock.cardBalance >> 0
    }

    def 'Should add a credit card object' () {
        given: 'a test object for CreditCardService'
        def testObj = new CreditCardService(creditCardValidatorMock, creditCardRepositoryMock)

        and:'Credit card number is validated successfully and the card has been persisted in the repository'
        creditCardValidatorMock.validate(creditCardMock) >> true
        creditCardRepositoryMock.save(creditCardEntityMock) >> creditCardEntityMock

        and: 'CreditCardRepository checks if the card number already exists before persisting'
        creditCardRepositoryMock.findById(Long.valueOf(creditCardEntityMock.cardNumber)) >> Optional.empty()

        when: 'invoke add method'
        def result = testObj.add(creditCardMock)

        then:
        result.serviceError == null
        result.response == creditCardMock

        and:'verify the invocations below has been made once only'
        1 * creditCardRepositoryMock.findById(_) >> Optional.empty()
        1 * creditCardRepositoryMock.save(_) >> creditCardEntityMock
        1 * creditCardValidatorMock.validate(_) >> true
    }

    def 'Should not add a credit card object if the card number is invalid' () {
        given: 'a test object for CreditCardService'
        def testObj = new CreditCardService(creditCardValidatorMock, creditCardRepositoryMock)

        and:'Credit card number is not validated'
        creditCardValidatorMock.validate(creditCardMock) >> false

        when: 'invoke add method'
        def result = testObj.add(creditCardMock)

        then:
        result.serviceError == ServiceResponse.ServiceError.VALIDATION_ERROR
        result.response == null

        and:'verify the invocations below has been made'
        0 * creditCardRepositoryMock.findById(_) >> Optional.empty()
        0 * creditCardRepositoryMock.save(_) >> creditCardEntityMock
        1 * creditCardValidatorMock.validate(_) >> false
    }

    def 'Should not add a credit card object if the card already exists' () {
        given: 'a test object for CreditCardService'
        def testObj = new CreditCardService(creditCardValidatorMock, creditCardRepositoryMock)

        and:'Credit card number is validated successfully'
        creditCardValidatorMock.validate(creditCardMock) >> true

        and: 'CreditCardRepository findById returns some existing creditCardEntity'
        creditCardRepositoryMock.findById(Long.valueOf(creditCardEntityMock.cardNumber)) >> Optional.of(CreditCardEntity)

        when: 'invoke add method'
        def result = testObj.add(creditCardMock)

        then:
        result.serviceError == ServiceResponse.ServiceError.ALREADY_EXISTS
        result.response == null

        and:'verify the invocations below has been made'
        0 * creditCardRepositoryMock.save(_) >> creditCardEntityMock
        1 * creditCardValidatorMock.validate(_) >> true
        1 * creditCardRepositoryMock.findById(_) >> Optional.of(creditCardEntityMock)
    }

    def 'Should not add a credit card object if there is persistence exception' () {
        given: 'a test object for CreditCardService'
        def testObj = new CreditCardService(creditCardValidatorMock, creditCardRepositoryMock)

        and:'Credit card number is validated successfully'
        creditCardValidatorMock.validate(creditCardMock) >> true

        and: 'CreditCardRepository findById returns an empty optional value'
        creditCardRepositoryMock.findById(Long.valueOf(creditCardEntityMock.cardNumber)) >> Optional.empty()
        creditCardRepositoryMock.save(creditCardEntityMock) >> { throw new PersistenceException("Some exception")}

        when: 'invoke add method'
        def result = testObj.add(creditCardMock)

        then:
        result.serviceError == ServiceResponse.ServiceError.DATABASE_ERROR
        result.response == null

        and:'verify the invocations below has been made'
        1 * creditCardRepositoryMock.save(_) >> { throw new PersistenceException("Some exception")}
        1 * creditCardValidatorMock.validate(_) >> true
        1 * creditCardRepositoryMock.findById(_) >> Optional.empty()
    }

    def 'Should get a list of credit cards' () {
        given: 'a test object for CreditCardService'
        def testObj = new CreditCardService(creditCardValidatorMock, creditCardRepositoryMock)

        and:'CreditCardRepo fetches all the credit cards'
        creditCardRepositoryMock.findAll() >> Arrays.asList(creditCardEntityMock, creditCardEntityMock1, creditCardEntityMock2)

        when: 'invoke get method'
        def result = testObj.get()

        then:
        result.serviceError == null
        result.response.size() == 3

        and:'verify the invocations below has been made once only'
        1 * creditCardRepositoryMock.findAll() >> Arrays.asList(creditCardEntityMock, creditCardEntityMock1, creditCardEntityMock2)
    }

    def 'Should get a list of credit cards if there is persistence exception' () {
        given: 'a test object for CreditCardService'
        def testObj = new CreditCardService(creditCardValidatorMock, creditCardRepositoryMock)

        and:'CreditCardRepo encounters an exception while fetching a list of cards'
        creditCardRepositoryMock.findAll() >> { throw new PersistenceException("Some exception")}

        when: 'invoke get method'
        def result = testObj.get()

        then:
        result.serviceError == ServiceResponse.ServiceError.DATABASE_ERROR
        result.response == null

        and:'verify the invocations below has been made once only'
        1 * creditCardRepositoryMock.findAll() >> { throw new PersistenceException("Some exception")}
    }
}
