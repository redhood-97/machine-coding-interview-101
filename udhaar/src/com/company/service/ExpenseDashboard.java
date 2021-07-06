package com.company.service;

import com.company.models.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpenseDashboard {

    HashMap<String, User> userStore = new HashMap<>();
    HashMap<String, HashMap<String, Float>> balances = new HashMap<>();
    HashMap<String, Object> history = new HashMap<>();

    public ExpenseDashboard() {}

    public ExpenseDashboard(HashMap<String, User> userStore,
                            HashMap<String, HashMap<String, Float>> balances,
                            HashMap<String, Object> history) {
        this.userStore = userStore;
        this.balances = balances;
        this.history = history;
    }

    public HashMap<String, User> getUserStore() { return this.userStore; }
    public void setUserStore(HashMap<String, User> userStore) { this.userStore = userStore; }

    public HashMap<String, HashMap<String, Float>> getBalances() { return this.balances; }
    public void setBalances(HashMap<String, HashMap<String, Float>> balances) { this.balances = balances; }

    public HashMap<String, Object> getHistory() { return this.history; }
    public void setHistory(HashMap<String, Object> history) { this.history = history; }

    public void addUser(String userId, User user) {

        if(this.userExists(userId)) {
            System.out.println("The user id already exists. Please try again.");
            return;
        }
        HashMap<String, Float> newPeerInBalance = new HashMap<>();
        for(Map.Entry<String, User> entry: this.userStore.entrySet()) {
            newPeerInBalance.put(entry.getKey(), 0.00f);
        }
        for(Map.Entry<String, HashMap<String, Float>> bal: this.balances.entrySet()) {
            bal.getValue().put(userId, 0.00f);
        }
        this.balances.put(userId, newPeerInBalance);
        this.userStore.put(userId, user);

        System.out.println("The user with id = " + userId + " has been successfully added.");
    }

    public void viewBalance() {
        System.out.println("Summary of Udhaar = ");
        for(Map.Entry<String, HashMap<String, Float>> et: this.balances.entrySet()) {
            System.out.println("For user = [" + et.getKey() + "]");
            HashMap<String, Float> peers = et.getValue();
            for(Map.Entry<String, Float> pet: peers.entrySet()) {
                if(pet.getValue() < 0.00f) {
                    System.out.println(" --> Owes Rs." + Float.toString(pet.getValue()*(-1.00f)) + " to user = [" + pet.getKey() + "]");
                }else {
                    System.out.println(" --> Gets Rs." + Float.toString(pet.getValue()) + " from user = [" + pet.getKey() + "]");
                }
            }
        }
    }

    public boolean userExists(String userId) {
        return this.userStore.containsKey(userId);
    }

    public void updateBalances(String userId, HashMap<String, Float> spMap) {
        HashMap<String, Float> peerBalance = this.balances.get(userId);
        for(Map.Entry<String, Float> entry: spMap.entrySet()) {
            // 1st modification - positive addition (means receiving money)
            if(!entry.getKey().equals(userId)) {
                Float curBal = peerBalance.get(entry.getKey());
                curBal += entry.getValue();
                peerBalance.put(entry.getKey(), curBal);
                this.balances.put(userId, peerBalance); // Update the internal balance hashmap against the user

                // 2nd modification - negative addition (means owing money)
                HashMap<String, Float> owingUserPeerBalance = this.balances.get(entry.getKey());
                curBal = owingUserPeerBalance.get(userId);
                curBal -= entry.getValue();
                owingUserPeerBalance.put(userId, curBal);
                this.balances.put(entry.getKey(), owingUserPeerBalance);
            }
        }
    }

    public void addExpenseAndCalculateSplit() {
        HashMap<String, Float> resultSplitMap = new HashMap<>();
        Scanner sc = new Scanner(System.in);

        Float amount;
        System.out.println("What is the total amount spent ?");
        amount = sc.nextFloat();

        String userId = new String();
        boolean userExists = false;
        while(!userExists) {
            System.out.println("What is the user id of the payee ?");
            userId = sc.next();
            if (!this.userStore.containsKey(userId)) {
                System.out.println("The user id entered does not exist. Please, try again.");
                continue;
            }
            userExists = true;
        }

        String description;
        System.out.println("What was this payment for ?");
        description = sc.next();

        User paidBy;

        Expense expense = new Expense(amount, this.userStore.get(userId), description);

        System.out.println("How would you like to split ? [Equal/Unequal/Percentage]");
        String type = sc.next();

        if(type.equalsIgnoreCase("EQUAL")) {

            List<String> userList = new ArrayList<>();
            System.out.println("The amount is to be equally divided.");
            System.out.println("Enter the user ids in a comma separated fashion = (No spaces)");
            String usrs = sc.next();
            userList = Stream.of(usrs.split(",", -1)).collect(Collectors.toList());
            userList.add(userId);
            System.out.println("The list of users to be passed in equal split class = ");
            System.out.println(userList);
            EqualSplit eqSplit = new EqualSplit(expense, userList);
            this.updateBalances(userId, eqSplit.getSplits());

        }else if(type.equalsIgnoreCase("UNEQUAL")) {

            HashMap<String, Float> splitMap = new HashMap<>();
            Float tmpAmount = 0.00f;
            boolean goAgain = true;
            while(goAgain) {
                System.out.println("Enter user id and the amount to be split = ");
                String usr = sc.next();
                if(!this.userExists(userId)) {
                    System.out.println("You seem to have entered a non-existent user. Please go again.");
                    continue;
                }
                Float amt = sc.nextFloat();
                tmpAmount += amt;
                if(amount < tmpAmount) {
                    System.out.println("The added split amounts have exceeded the amount spent. Please enter again.");
                    continue;
                }
                splitMap.put(usr, amt);
                System.out.println("Are there more users ? [Y/N]");
                char toContinue = sc.next().charAt(0);
                if(toContinue == 'Y') {
                    continue;
                }
                goAgain = false;
            }
            UnEqualSplit unEqSplit = new UnEqualSplit(expense, splitMap);
            this.updateBalances(userId, unEqSplit.getSplits());

        }else if(type.equalsIgnoreCase("PERCENTAGE")) {
            HashMap<String, Float> percSplitMap = new HashMap<>();
            Float addedPerc = 0.00f;
            boolean goAgain = true;
            while(goAgain) {
                System.out.println("Enter the user id and the percentage share = ");
                String usr = sc.next();
                if(!this.userExists(usr)) {
                    System.out.println("You seem to have entered a non-existent user. Please go again.");
                    continue;
                }
                Float perc = sc.nextFloat();
                addedPerc += perc;
                if(addedPerc > 100.00f) {
                    System.out.println("The total percentage share has crossed 100.00%. Please enter again for the current user.");
                    continue;
                }
                percSplitMap.put(usr, perc);
                System.out.println("Are there more users ? [Y/N]");
                char toContinue = sc.next().charAt(0);
                if(toContinue == 'Y') {
                    continue;
                }
                goAgain = false;
            }
            PercentageSplit percSplit = new PercentageSplit(expense, percSplitMap);
            this.updateBalances(userId, percSplit.getSplits());
        }

        this.viewBalance();
    }

}
