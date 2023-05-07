package com.leafbank;

import com.google.firebase.Timestamp;

public class HistoryItem {
    private String fromNumber;
    private String fromUser;
    private String toNumber;
    private String toUser;
    private Timestamp date;
    private double amount;
    private String  direction;

    public HistoryItem() {
    }

    public HistoryItem(String fromUser, String fromNumber,  String toUser, String toNumber, Timestamp date, double amount) {
        this.fromNumber = fromNumber;
        this.fromUser = fromUser;
        this.toNumber = toNumber;
        this.toUser = toUser;
        this.date = date;
        this.amount = amount;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public String getToNumber() {
        return toNumber;
    }

    public Timestamp getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    String  getDirection() {
        return direction;
    }

    void setDirection(String direction) {
        this.direction = direction;
    }
}
