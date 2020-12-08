package com.gateway.creditcard

import com.gateway.creditcard.repository.CreditCardRepository
import model.CreditCard
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreditCardIntegrationSpecification extends Specification {

    @Autowired
    private TestRestTemplate testRestTemplate

    @Autowired
    private CreditCardRepository creditCardRepository

    @LocalServerPort
    private int port

    //Clean up job before each test begun an execution
    def setup() {
        creditCardRepository.deleteAllInBatch()
    }

    def 'Should return 201-CREATED when the new credit card has been added'() {

        when:
        def response = testRestTemplate.exchange(accessCreditCardGatewayOnThePort(
                "api/creditcard/v1/add")
                , HttpMethod.POST
                , new CreditCard("someName", '4444333322221111')
                , String.class
        )

        then:
        response.statusCode == HttpStatus.CREATED
    }

    def 'Should return 400-BAD REQUEST when the card request is not validated'() {
        when:
        def response = testRestTemplate.exchange(accessCreditCardGatewayOnThePort(
                "api/creditcard/v1/add")
                , HttpMethod.POST
                , new CreditCard(null, '4444333322221111')
                , String.class
        )

        then:
        response.statusCode == HttpStatus.BAD_REQUEST
    }

    def 'Should return 409-CONFLICT when the card already exists'() {
        given: 'A credit card is created'
        def createResponse1 = testRestTemplate.exchange(accessCreditCardGatewayOnThePort(
                "api/creditcard/v1/add")
                , HttpMethod.POST
                , new CreditCard("someName", '4444333322221111')
                , String.class
        )
        when: 'The same card number is entered'
        def response = testRestTemplate.exchange(accessCreditCardGatewayOnThePort(
                "api/creditcard/v1/add")
                , HttpMethod.POST
                , new CreditCard('someOtherName','4444333322221111')
                , String.class
        )

        then:
        response.statusCode == HttpStatus.CONFLICT
    }

    def 'Should return 200-OK when get all credit cards end point has been triggered'() {
        given: 'A credit card1 is created'
        testRestTemplate.exchange(accessCreditCardGatewayOnThePort(
                "api/creditcard/v1/add")
                , HttpMethod.POST
                , new CreditCard('someName', '4444333322221111')
                , String.class
        )
        and: 'Another credit card has been created'
       testRestTemplate.exchange(accessCreditCardGatewayOnThePort(
                "api/creditcard/v1/add")
                , HttpMethod.POST
                , new CreditCard('someOtherName', '4444333322221111')
                , String.class
        )

        when:
        def response = testRestTemplate.exchange(accessCreditCardGatewayOnThePort(
                "api/creditcard/v1/get")
                , HttpMethod.POST
                , null
                , CreditCard[].class
        )

        then:
        response.statusCode == HttpStatus.OK
        response.body.size() == 2
    }

    private String accessCreditCardGatewayOnThePort(String uri) {
        return "http://localhost:" + port + uri
    }
}
