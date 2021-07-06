package com.company.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PercentageSplit extends Split {

    private HashMap<String, Float> percentageWiseSplits;
    private HashMap<String, Float> splits;

    public PercentageSplit() {}

    public PercentageSplit(Expense expense, HashMap<String, Float> percentageWiseSplits) {
        super(expense);
        this.percentageWiseSplits = percentageWiseSplits;
        this.calculate();
    }

    public void setPercentageWiseSplits(HashMap<String, Float> percentageWiseSplits) { this.percentageWiseSplits = percentageWiseSplits; }

    public HashMap<String, Float> getPercentageWiseSplits() { return this.percentageWiseSplits; }

    public void setSplits(HashMap<String, Float> splits) { this.splits = splits; }

    public HashMap<String, Float> getSplits() { return this.splits; }

    @Override
    public void calculate() {
        if(!this.areSplitParametersOkay(this.percentageWiseSplits)) {
            System.out.println("Invalid percent split error !!!");
            return;
        }
        HashMap<String, Float> tmp = new HashMap<>();
        Iterator<Map.Entry<String, Float>> itr = this.percentageWiseSplits.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<String, Float> entry = itr.next();
            Float percAmount = (entry.getValue()*this.getExpense().getAmount())/100.00f;
            tmp.put(entry.getKey(), percAmount);
        }
        this.setSplits(tmp);
    }

    public boolean areSplitParametersOkay(HashMap<String, Float> percentSplits) {
        Float totalPercent = 0.00f;
        for(Map.Entry<String, Float> entry: percentSplits.entrySet()) {
            if(entry.getValue() < 0.00f || entry.getValue() > 100.00f) {
                System.out.println("The split percent for userId = " + entry.getValue() + " is invalid !");
                return false;
            }
            totalPercent += entry.getValue();
        }
        if(totalPercent > 100.00f) {
            System.out.println("The total percent split doesn't add up to 100");
            return false;
        }

        return true;
    }
}
