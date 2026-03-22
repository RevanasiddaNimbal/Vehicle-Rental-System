package UI;

import customer.controller.CustomerController;
import util.InputUtil;

import java.util.Scanner;

public class AdminsCustomerMenu implements UsersMenu {
    private final CustomerController customerController;
    private final Scanner input;

    public AdminsCustomerMenu(Scanner input, CustomerController customerController) {
        this.input = input;
        this.customerController = customerController;
    }

    @Override
    public void show(String adminId) {
        int choice;
        while (true) {
            System.out.println("======= Customer Management Menu =========");
            System.out.println("4.  View All Customers");
            System.out.println("5.  Activate Customer");
            System.out.println("6.  Deactivate Customer");
            System.out.println("0. Back");
            choice = InputUtil.readPositiveInt(input, "Enter your choice");
            switch (choice) {
                case 1:
                    customerController.viewCustomers();
                    break;
                case 2:
                    customerController.activateCustomer(input);
                    break;
                case 3:
                    customerController.deactivateCustomer(input);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
