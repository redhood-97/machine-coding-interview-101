package com.company.models;

import java.util.HashMap;
import java.util.List;

public class EqualSplit extends Split {

    private List<String> users;
    private HashMap<String, Float> splits;

    public EqualSplit(Expense expense, List<String> users) {
        super(expense);
        this.users = users;
        this.calculate();
    }

    public HashMap<String, Float> getSplits() { return this.splits; }

    public void setSplits(HashMap<String, Float> splits) { this.splits = splits; }

    @Override
    public void calculate() {
        Float splitAmount = this.getExpense().getAmount()/this.users.size();
        HashMap<String, Float> tmp = new HashMap<>();
        for(String user: this.users) {
            tmp.put(user, splitAmount);
        }
        this.setSplits(tmp);
        System.out.println("The splits map calculated for EQUAL split = ");
        System.out.println(this.splits);
    }

}
