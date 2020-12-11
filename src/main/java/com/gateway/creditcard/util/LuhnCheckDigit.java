package com.gateway.creditcard.util;

import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

/**
 * LunaCheck algorithm is a general purpose algorithm.
 * It can be used from any validation context like credit card numbers, IMEI Numbers etc.
 * Hence to keep it generalized and loosely coupled this class has been defined as a separate component.
 **/

@Component
public class LuhnCheckDigit {

    public boolean checkDigit(String numberToBeValidated) {
        int[] number = convert2Array(numberToBeValidated);
        return IntStream.range(0, number.length)
                //Its an algo to check the simple data entry errors in the algo
                //last digit is always check digit in the algo
                //can be validated using two methods
                //0^0 is 0
                //For every even position from right we take sum of digits of double the number and for odd position we take a number directly
                //checks for an even lenght position         //double the number and sum individual digits, for odd position add the num as it is
                .map(i -> (((i % 2) ^ (number.length % 2)) == 0) ? ((2 * number[i]) / 10 + (2 * number[i]) % 10) : number[i])
                .sum() % 10 == 0;
    }

    private int[] convert2Array(String number) {
        int[] toNumberArray = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            toNumberArray[i] = number.charAt(i) - '0';
        }
        return toNumberArray;
    }
}
