package customer.controller;

import UI.UserPrinter;
import customer.model.Customer;
import customer.service.CustomerService;
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

        if (service.getCustomerById(id) == null) {
            System.out.println("Customer ID Not Found");
            return;
        }

        if (service.deactivateCustomerById(id)) {
            System.out.println("Blocked Customer Successfully.");
        } else {
            System.out.println("Failed to block Customer.");
        }
    }

    public void activateCustomer(Scanner input) {
        String id = InputUtil.readString(input, "Enter Customer ID");

        if (service.getCustomerById(id) == null) {
            System.out.println("Customer ID Not Found");
            return;
        }

        if (service.activateCustomerById(id)) {
            System.out.println("Activated Customer Successfully.");
        } else {
            System.out.println("Failed to activate Customer.");
        }
    }

    public void resetPassword(Scanner input, String customerId) {
        if (customerId == null) {
            System.out.println("Customer Id is required");
            return;
        }
        if (service.resetPassword(input, customerId)) {
            System.out.println("Reset Password Successfully.");
        } else {
            System.out.println("Failed to reset Password.Please try again");
        }
    }

    public void updateCustomer(Scanner input, String Id) {
        Customer customer = service.getCustomerById(Id);

        if (customer == null) {
            System.out.println("Customer ID Not Found");
            return;
        }

        System.out.println("----Update Customer----");
        System.out.println("1. Name");
        System.out.println("2. Email address");
        System.out.println("3. Phone number");
        System.out.println("4. Address");
        System.out.println("5. Driving License Number");
        System.out.println("0. Back");

        int choice = InputUtil.readPositiveInt(input, "Enter choice");

        switch (choice) {
            case 1:
                String name = InputUtil.readString(input, "Enter new full name");
                customer.setName(name);
                break;

            case 2:
                String email = InputUtil.readString(input, "Enter new email address");
                customer.setEmail(email);
                break;

            case 3:
                String phone = InputUtil.readString(input, "Enter new phone number");
                customer.setPhone(phone);
                break;

            case 4:
                String address = InputUtil.readString(input, "Enter new address");
                customer.setAddress(address);
                break;

            case 5:
                String license = InputUtil.readString(input, "Enter new Driving License Number");
                customer.setDrivingLicenseNumber(license);
                break;
            case 0:
                return;

            default:
                System.out.println("Invalid choice");
                return;
        }

        if (service.updateCustomer(customer)) {
            System.out.println("Customer Updated Successfully");
        } else {
            System.out.println("Failed to update Customer.");
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
