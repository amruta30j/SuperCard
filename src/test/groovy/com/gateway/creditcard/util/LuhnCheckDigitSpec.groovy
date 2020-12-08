package com.gateway.creditcard.util

import spock.lang.Specification

class LuhnCheckDigitSpec extends Specification {

    def 'Should validate a valid number using Luhn10 algorithm and return TRUE'() {

        given: 'a test object for LuhnCheckDigit'
        def testObj = new LuhnCheckDigit()

        when: 'invoke a checkDigit funtion'
        def result = testObj.checkDigit('4444333322221111')

        then:
        result.equals(true)
    }

    def 'Should validate an invalid number using Luhn10 algorithm and return FALSE'() {

        given: 'a test object for LuhnCheckDigit'
        def testObj = new LuhnCheckDigit()

        when: 'invoke a checkDigit funtion'
        def result = testObj.checkDigit('122000000000004')

        then:
        result.equals(false)
    }
}
