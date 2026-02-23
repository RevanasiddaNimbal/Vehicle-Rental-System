package UI;


import util.InputUtil;

import java.util.Scanner;

public class CustomerMenu implements Menu {
    private final Scanner input;

    public CustomerMenu(Scanner input) {
        this.input = input;
    }

    @Override
    public void show() {
        int choice;
        while (true) {

            System.out.println("\n----- CUSTOMER MANAGEMENT -----");
            System.out.println("1. Register New Customer");
            System.out.println("2. View All Customers");
            System.out.println("3. Search Customer");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("0. Back");

            choice = InputUtil.readPositiveInt(input, "Enter your choice");

            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
