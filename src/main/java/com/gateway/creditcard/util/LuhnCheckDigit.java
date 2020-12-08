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
