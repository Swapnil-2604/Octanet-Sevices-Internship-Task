package com.atm.serviceImpl;

import com.atm.model.account;
import com.atm.service.Atm;

import java.sql.*;
import java.util.Scanner;

public class Atm_impl implements Atm {
    account a = new account(); // Instance of the account class
    Scanner sc = new Scanner(System.in);

    // Utility method to establish database connection
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/atm", "root", "root");
    }

    // Method to fetch account data and populate the account object
    private boolean loadAccount(String pin) {
        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM atminfo WHERE pin = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Populate the account object with data from the database
                a.setPin(Integer.parseInt(pin));
                a.setBalance(rs.getInt("balance"));
                return true; // Successfully loaded account
            } else {
                System.err.println("Invalid PIN. Please try again.");
                return false; // Account not found
            }
        } catch (Exception e) {
            System.err.println("An error occurred while loading the account.");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void checkbalance() {
        System.out.println("Enter your PIN:");
        String pin = sc.next();

        // Load account details
        if (loadAccount(pin)) {
            System.out.println("Your Current Balance is: " + a.getBalance());
        } else {
            handleWrongPin();
        }
    }

    @Override
    public void withdraw() {
        System.out.println("Enter your PIN:");
        String pin = sc.next();

        // Load account details
        if (loadAccount(pin)) {
            System.out.println("Enter the amount to withdraw:");
            int amount = sc.nextInt();

            // Validate withdrawal
            if (amount > 0 && amount <= a.getBalance() && amount % 100 == 0) {
                int newBalance = a.getBalance() - amount;
                try (Connection con = getConnection()) {
                    String sql = "UPDATE atminfo SET balance = ? WHERE pin = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, newBalance);
                    ps.setInt(2, a.getPin());
                    ps.executeUpdate();

                    a.setBalance(newBalance); // Update the account object
                    System.out.println("Withdrawal Successful! Remaining Balance: " + a.getBalance());
                } catch (Exception e) {
                    System.err.println("An error occurred during withdrawal.");
                    e.printStackTrace();
                }
            } else {
                System.err.println("Invalid withdrawal amount. Ensure it is <= balance and in multiples of 100.");
            }
        } else {
            handleWrongPin();
        }
    }

    @Override
    public void deposit() {
        System.out.println("Enter your PIN:");
        String pin = sc.next();

        // Load account details
        if (loadAccount(pin)) {
            System.out.println("Enter the amount to deposit:");
            int amount = sc.nextInt();

            // Validate deposit
            if (amount > 0) {
                int newBalance = a.getBalance() + amount;
                try (Connection con = getConnection()) {
                    String sql = "UPDATE atminfo SET balance = ? WHERE pin = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, newBalance);
                    ps.setInt(2, a.getPin());
                    ps.executeUpdate();

                    a.setBalance(newBalance); // Update the account object
                    System.out.println("Deposit Successful! Current Balance: " + a.getBalance());
                } catch (Exception e) {
                    System.err.println("An error occurred during deposit.");
                    e.printStackTrace();
                }
            } else {
                System.err.println("Invalid deposit amount.");
            }
        } else {
            handleWrongPin();
        }
    }

    @Override
    public void changepin() {
        System.out.println("Enter your current PIN:");
        String currentPin = sc.next();

        // Load account details
        if (loadAccount(currentPin)) {
            System.out.println("Enter a new 4-digit PIN:");
            int newPin = sc.nextInt();

            // Validate new PIN
            if (String.valueOf(newPin).length() == 4) {
                try (Connection con = getConnection()) {
                    String sql = "UPDATE atminfo SET pin = ? WHERE pin = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, newPin);
                    ps.setInt(2, a.getPin());
                    ps.executeUpdate();

                    a.setPin(newPin); // Update the account object
                    System.out.println("PIN changed successfully.");
                } catch (Exception e) {
                    System.err.println("An error occurred while changing the PIN.");
                    e.printStackTrace();
                }
            } else {
                System.err.println("Invalid PIN. It must be exactly 4 digits.");
            }
        } else {
            handleWrongPin();
        }
    }

    private void handleWrongPin() {
        while (true) {
            System.out.println("What would you like to do?\n1) Retry\n2) Exit");
            int choice = sc.nextInt();

            if (choice == 1) {
                return; // Exit the method to retry
            } else if (choice == 2) {
                System.out.println("Exiting...");
                return; // Exit the method to terminate the operation
            } else {
                System.err.println("Invalid choice. Please try again.");
            }
        }
    }
}
