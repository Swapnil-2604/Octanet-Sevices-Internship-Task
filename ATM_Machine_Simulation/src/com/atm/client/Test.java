package com.atm.client;

import com.atm.serviceImpl.Atm_impl;

import java.util.Scanner;

public class Test {
    Scanner sc = new Scanner(System.in);
     Atm_impl ai = new Atm_impl();

    public void choice() {
        while (true) {
            System.out.println("1). Check balance \n2). Withdraw \n3). Deposit \n4). Change pin \n5). Exit");
            System.out.println("Enter your choice:");
            
            if (!sc.hasNextShort()) {
                System.err.println("Invalid input. Please enter a number.");
                sc.next();
                continue;
            }
            
            short no = sc.nextShort();
            switch (no) {
                case 1:
                    ai.checkbalance();
                    break;
                case 2:
                    ai.withdraw();
                    break;
                case 3:
                    ai.deposit();
                    break;
                case 4:
                    ai.changepin(); 
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return; // Exit the program
                default:
                    System.err.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        Test t = new Test();
        t.choice();
    }
}
