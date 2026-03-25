package UI;

import customer.controller.CustomerController;
import util.InputUtil;

import java.util.Scanner;

public class CustomerAccountMenu implements UsersMenu {
    private final Scanner input;
    private final CustomerController customerController;

    public CustomerAccountMenu(Scanner input, CustomerController customerController) {
        this.input = input;
        this.customerController = customerController;
    }

    @Override
    public void show(String customerId) {
        int choice;
        while (true) {
            try {
                System.out.println("\n========= Customer Account Management Menu ==============");
                System.out.println("1. Reset Password");
                System.out.println("2. Update My Account");
                System.out.println("0. Logout");
                choice = InputUtil.readPositiveInt(input, "Enter your choice");
                switch (choice) {
                    case 1:
                        customerController.resetPassword(input, customerId);
                        break;
                    case 2:
                        customerController.updateCustomer(input, customerId);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice.Please try again");

                }
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}
