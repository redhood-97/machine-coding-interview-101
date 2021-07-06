package com.company;

import com.company.models.User;
import com.company.service.ExpenseDashboard;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	    System.out.println("!!!! WELCOME TO UDHAAR !!!!");
        ExpenseDashboard expenseDashboard = new ExpenseDashboard();
        expenseDashboard.addUser("Eren", new User("Eren", "9123519655", "eren@gmail.com"));
        expenseDashboard.addUser("Kira", new User("Kira", "111111111", "kira@gmail.com"));
        expenseDashboard.addUser("Lelouch", new User("Lelouch", "111111111", "lelouch@gmail.com"));
        expenseDashboard.addUser("Levi", new User("Levi", "1111111111", "levi@gmail.com"));
        char flag = 'Y';
        Scanner sc = new Scanner(System.in);
        while(flag == 'Y') {
            System.out.println("*********************** Let's begin ***********************");
            expenseDashboard.addExpenseAndCalculateSplit();
            System.out.println("Want to add another expense ? [Y/N]");
            flag = sc.next().charAt(0);
        }
        System.out.println("*********************** Goodbye ***********************");
    }
}
