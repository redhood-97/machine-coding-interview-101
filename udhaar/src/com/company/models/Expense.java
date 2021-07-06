package com.company.models;

import java.util.HashMap;

public class Expense {

    private String description;
    private Float amount;
    private User paidBy;

    public Expense() {}

    public Expense(Float amount, User paidBy, String description) {
        this.amount = amount;
        this.paidBy = paidBy;
        this.description = description;
    }

    public Float getAmount() {
        return this.amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setPaidBy(User paidBy) {
        this.paidBy = paidBy;
    }

    public User getPaidBy() {
        return this.getPaidBy();
    }

    public String getDescription() { return this.description; }

    public void setDescription(String description) { this.description = description; }

}
