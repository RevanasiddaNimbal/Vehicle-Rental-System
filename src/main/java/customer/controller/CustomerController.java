package customer.controller;

import UI.UserPrinter;
import customer.model.Customer;
import customer.service.CustomerService;
import exception.DuplicateResourceException;
import exception.InactiveUserException;
import exception.ResourceNotFoundException;
import util.InputUtil;

import java.util.List;
import java.util.Scanner;

public class CustomerController {

    private final CustomerService service;
    private final UserPrinter<Customer> printer;

    public CustomerController(CustomerService service, UserPrinter<Customer> printer) {
        this.service = service;
        this.printer = printer;
    }

    public void deactivateCustomer(Scanner input) {
        String id = InputUtil.readString(input, "Enter Customer ID");
        try {
            if (service.deactivateCustomerById(id)) {
                System.out.println("Blocked Customer Successfully.");
            } else {
                System.out.println("Customer is already deactivated.");
            }
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void activateCustomer(Scanner input) {
        String id = InputUtil.readString(input, "Enter Customer ID");
        try {
            if (service.activateCustomerById(id)) {
                System.out.println("Activated Customer Successfully.");
            } else {
                System.out.println("Customer is already active.");
            }
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateCustomer(Scanner input, String id) {
        try {
            Customer customer = service.getCustomerById(id);
            boolean updating = true;
            boolean hasChanges = false;

            while (updating) {
                System.out.println("\n---- Update Customer ----");
                System.out.println("1. Name");
                System.out.println("2. Email address");
                System.out.println("3. Phone number");
                System.out.println("4. Address");
                System.out.println("5. Driving License Number");
                System.out.println("0. Save and Back");

                int choice = InputUtil.readPositiveInt(input, "Enter choice");

                switch (choice) {
                    case 1:
                        customer.setName(InputUtil.readString(input, "Enter new full name"));
                        hasChanges = true;
                        break;
                    case 2:
                        customer.setEmail(InputUtil.readValidEmail(input, "Enter new email address"));
                        hasChanges = true;
                        break;
                    case 3:
                        customer.setPhone(InputUtil.readValidPhone(input, "Enter new phone number"));
                        hasChanges = true;
                        break;
                    case 4:
                        customer.setAddress(InputUtil.readString(input, "Enter new address"));
                        hasChanges = true;
                        break;
                    case 5:
                        customer.setDrivingLicenseNumber(InputUtil.readString(input, "Enter new Driving License Number"));
                        hasChanges = true;
                        break;
                    case 0:
                        updating = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }

            if (hasChanges) {
                service.updateCustomer(customer);
                System.out.println("Customer Updated Successfully.");
            } else {
                System.out.println("No changes were made.");
            }

        } catch (InactiveUserException | ResourceNotFoundException | DuplicateResourceException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void resetPassword(Scanner input, String customerId) {
        String oldPassword = InputUtil.readString(input, "Enter your CURRENT password");
        String newPassword = InputUtil.readValidPassword(input, "Enter your NEW password");

        try {
            if (service.resetPassword(customerId, oldPassword, newPassword)) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("Failed to update password. Please try again later.");
            }
        } catch (IllegalArgumentException | InactiveUserException | ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewCustomers() {
        List<Customer> customers = service.getCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found");
            return;
        }
        printer.print(customers);
    }
}