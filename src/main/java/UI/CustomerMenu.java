package UI;


import authentication.model.UserRole;
import customer.controller.CustomerController;
import util.InputUtil;

import java.util.Scanner;

public class CustomerMenu implements UsersMenu {
    private final Scanner input;
    private final CustomerController customerController;

    public CustomerMenu(Scanner input, CustomerController customerController) {
        this.input = input;
        this.customerController = customerController;
    }

    @Override
    public void show(UserRole role, String customerId) {
        int choice;
        while (true) {
            System.out.println("\n========= CUSTOMER PANEL ==============");
            System.out.println("1. View Available Vehicles");
            System.out.println("2. Rent a Vehicle");
            System.out.println("3. Return a Vehicle");
            System.out.println("4. My Active Rental");
            System.out.println("5. My Rental History");
            System.out.println("6. Update My Account");
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
                case 6:
                    customerController.updateCustomer(input, customerId);
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
