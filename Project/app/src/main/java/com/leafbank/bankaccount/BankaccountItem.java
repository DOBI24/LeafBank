package com.leafbank.bankaccount;

public class BankaccountItem {
    private String number;
    private double balance;

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

    public double getBalance() {
        return balance;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public static String numberFormat(String number){
        StringBuilder ret = new StringBuilder(number);
        ret.insert(8, "-");
        return String.valueOf(ret);
    }


}
