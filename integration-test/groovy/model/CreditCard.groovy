package model

import org.springframework.http.HttpEntity

class CreditCard extends HttpEntity{
    public String name;
    public String number;

    CreditCard(String name, String number) {
        this.name = name
        this.number = number
    }
}
