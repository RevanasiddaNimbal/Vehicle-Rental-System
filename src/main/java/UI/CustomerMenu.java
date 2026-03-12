package UI;


import authentication.model.UserRole;
import util.InputUtil;

import java.util.Scanner;

public class CustomerMenu implements Menu {
    private final Scanner input;

    public CustomerMenu(Scanner input) {
        this.input = input;
    }

    @Override
    public void show(UserRole role) {
        int choice;
        while (true) {
            System.out.println("\n========= CUSTOMER PANEL ==============");
            System.out.println("1. View Available Vehicles");
            System.out.println("2. Rent a Vehicle");
            System.out.println("3. Return a Vehicle");
            System.out.println("4. My Active Rental");
            System.out.println("5. My Rental History");
            System.out.println("0. Logout");

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
                    System.out.println("Logged out from Customer panel.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
