package com.company.models;

import java.util.HashMap;

public abstract class Split {

    private Expense expense;

    public void setExpense(Expense expense) { this.expense = expense; }

    public Expense getExpense() { return this.expense; }

    public Split() {}

    public Split(Expense expense) {
        this.expense = expense;
    }

    public abstract void calculate();
}
