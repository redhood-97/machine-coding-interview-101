package com.company.models;

import java.util.HashMap;

public class UnEqualSplit extends Split {

    private HashMap<String, Float> individualSplit;
    private HashMap<String, Float> splits;

    public UnEqualSplit() {}

    public UnEqualSplit(Expense expense, HashMap<String, Float> individualSplit) {
        super(expense);
        this.individualSplit = individualSplit;
        this.calculate();
    }

    public void setIndividualSplit(HashMap<String, Float> individualSplit) { this.individualSplit = individualSplit; }

    public HashMap<String, Float> getIndividualSplit() { return this.individualSplit; }

    public void setSplits(HashMap<String, Float> splits) { this.splits = splits; }

    public HashMap<String, Float> getSplits() { return this.splits; }

    @Override
    public void calculate() {
        this.splits = this.individualSplit;
    }
}
