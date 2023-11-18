package com.example.bookshopapp.sms2FA.external;

public class Balance {

    private String balance;
    private String viberBalance;
    private String currency;

    public Balance() {

    }

    public Balance(String balance, String viberBalance) {
        this.balance = balance;
        this.viberBalance = viberBalance;
    }

    public String getBalance() {
        return this.balance;
    }

    public String getViberBalance() {
        return this.viberBalance;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return this.currency;
    }
}
