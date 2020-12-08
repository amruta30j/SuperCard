package com.gateway.creditcard.controller.model;

/**
 * This is a generic domain object which,
 * can used to receive a request as well as sending a response
 */
public class CreditCard {

    private String name;
    //MAX Value of long is 19 digits but it will not support 19 digit numbers larger than MAX_VALUE hence String is used
    private String number;
    private double limit;

    public CreditCard(String name, String number, double limit) {
        this.name = name;
        this.number = number;
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }
}
