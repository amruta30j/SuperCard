package com.gateway.creditcard.controller

import com.gateway.creditcard.controller.model.CreditCard
import com.gateway.creditcard.service.CreditCardService
import com.gateway.creditcard.util.response.ServiceResponse
import org.springframework.http.HttpStatus
import spock.lang.Specification

class CreditCardControllerSpec extends Specification {

    def creditCardServiceMock = Mock(CreditCardService)
    def creditCardMock = Mock(CreditCard)
    def creditCardMock1 = Mock(CreditCard)
    def creditCardMock2 = Mock(CreditCard)
    def serviceResponseMock = Mock(ServiceResponse)

    def setup() {
        creditCardMock.name >> 'SomeCreditCardHolderName'
        creditCardMock.number >> '4444333322221111'
        creditCardMock.limit >> 0

        creditCardMock1.name >> 'SomeCreditCardHolderName'
        creditCardMock1.number >> '4444333322221111'
        creditCardMock1.limit >> 350.50

        creditCardMock2.name >> 'SomeCreditCardHolderName'
        creditCardMock2.number >> '4444333322221111'
        creditCardMock2.limit >> 3466.78
    }

    def 'Should respond with 201-CREATED when the credit card has been successfully created'() {
        given:'a test object for CreditCardController'
        def testObj = new CreditCardController(creditCardServiceMock)

        and: 'ServiceResponse object with no service error and some response object'
        serviceResponseMock.response >> creditCardMock
        serviceResponseMock.serviceError >> null


        creditCardServiceMock.add(creditCardMock) >> serviceResponseMock

        when:'invoke the add credit card endpoint'
        def result = testObj.add(creditCardMock)

        then:
        result.statusCode == HttpStatus.CREATED
        1 * creditCardServiceMock.add(_) >> serviceResponseMock
    }

    def 'Should respond with 409-Conflict when the credit card number is already present' () {
        given:'a test object for CreditCardController'
        def testObj = new CreditCardController(creditCardServiceMock)

        and: 'ServiceResponse object with service error'
        serviceResponseMock.response >> null
        serviceResponseMock.serviceError >> ServiceResponse.ServiceError.ALREADY_EXISTS

        and: 'CreditCardServiceMock will return a ServiceResponse mock object'
        creditCardServiceMock.add(creditCardMock) >> serviceResponseMock

        when:'invoke the add credit card endpoint'
        def result = testObj.add()

        then:
        result.statusCode== HttpStatus.CONFLICT
        1 * creditCardServiceMock.add(_) >> serviceResponseMock
    }

    def 'Should respond with 400-BAD REQUEST when the credit card number is not validated' () {
        given:'a test object for CreditCardController'
        def testObj = new CreditCardController(creditCardServiceMock)

        and: 'ServiceResponse object with service error'
        serviceResponseMock.response >> null
        serviceResponseMock.serviceError >> ServiceResponse.ServiceError.VALIDATION_ERROR

        and: 'CreditCardServiceMock will return a ServiceResponse mock object'
        creditCardServiceMock.add(creditCardMock) >> serviceResponseMock

        when:'invoke the add credit card endpoint'
        def result = testObj.add()

        then:
        result.statusCode== HttpStatus.BAD_REQUEST
        1 * creditCardServiceMock.add(_) >> serviceResponseMock
    }

    def 'Should respond with 500-INTERNAL SERVER ERROR when the credit card number is not validated' () {
        given:'a test object for CreditCardController'
        def testObj = new CreditCardController(creditCardServiceMock)

        and: 'ServiceResponse object with service error'
        serviceResponseMock.response >> null
        serviceResponseMock.serviceError >> ServiceResponse.ServiceError.DATABASE_ERROR

        and: 'CreditCardServiceMock will return a ServiceResponse mock object'
        creditCardServiceMock.add(creditCardMock) >> serviceResponseMock

        when: 'invoke the add credit card endpoint'
        def result = testObj.add()

        then:'the http status should be created and verify that the creditCardServiceMock.add method has been called exactly once'
        result.statusCode== HttpStatus.INTERNAL_SERVER_ERROR
        1 * creditCardServiceMock.add(_) >> serviceResponseMock
    }

    def 'Should respond with 200-OK with a list of credit card objects'() {
        given:'a test object for CreditCardController'
        def testObj = new CreditCardController(creditCardServiceMock)

        and: 'ServiceResponse object with service error'
        serviceResponseMock.response >> Arrays.asList(creditCardMock, creditCardMock1, creditCardMock2)
        serviceResponseMock.serviceError >> null

        and: 'CreditCardServiceMock will return a ServiceResponse mock object'
        creditCardServiceMock.get() >> serviceResponseMock

        when: 'invoke the get credit card endpoint'
        def result = testObj.get()

        then:'the http status should be 200-OK and verify that the creditCardServiceMock.get method has been called exactly once'
        result.body.size() == 3
        result.statusCode== HttpStatus.OK
        1 * creditCardServiceMock.get() >> serviceResponseMock
    }

    def 'Should respond with 500-Internal Server error with no response body'() {
        given:'a test object for CreditCardController'
        def testObj = new CreditCardController(creditCardServiceMock)

        and: 'ServiceResponse object with some service error'
        serviceResponseMock.response >> null
        serviceResponseMock.serviceError >> ServiceResponse.ServiceError.DATABASE_ERROR

        and: 'CreditCardServiceMock will return a ServiceResponse mock object'
        creditCardServiceMock.get() >> serviceResponseMock

        when: 'invoke the get credit card endpoint'
        def result = testObj.get()

        then:'the http status should be 500-Internal Server error and verify that the creditCardServiceMock.get method has been called exactly once'
        result.statusCode== HttpStatus.INTERNAL_SERVER_ERROR
        1 * creditCardServiceMock.get() >> serviceResponseMock
    }
}
