package com.leafbank.bankaccount;

public class BankaccountItem {
    private String number;
    private float balance;

    private String ownerID;

    public BankaccountItem() {
    }

    public BankaccountItem(String number, float balance, String ownerID) {
        this.number = number;
        this.balance = balance;
        this.ownerID = ownerID;
    }

    public String getNumber() {
        return number;
    }

    public float getBalance() {
        return balance;
    }

    public String getOwnerID() {
        return ownerID;
    }

    static String numberFormat(String number){
        StringBuilder ret = new StringBuilder(number);
        ret.insert(8, "-");
        return String.valueOf(ret);
    }
}
