package customer.service;

import customer.model.Customer;
import customer.repository.CustomerRepo;
import util.InputUtil;
import util.PasswordUtil;

import java.util.List;
import java.util.Scanner;

public class CustomerService {
    private final CustomerRepo repository;

    public CustomerService(CustomerRepo repository) {
        this.repository = repository;
    }

    public boolean addCustomer(Customer customer) {
        if (repository.findByEmail(customer.getEmail()) != null) {
            System.out.println("Customer already exists");
            return false;
        }
        return repository.save(customer);
    }

    public boolean updateCustomer(Customer customer) {
        if (repository.findById(customer.getId()) == null) {
            return false;
        }
        return repository.update(customer);
    }

    public boolean resetPassword(Scanner input, String customerId) {
        Customer customer = repository.findById(customerId);
        if (customer == null) {
            System.out.println("Customer not found");
            return false;
        }
        String oldPassword = InputUtil.readValidPassword(input, "Enter your old password");
        String newPassword = InputUtil.readValidPassword(input, "Enter your new password");
        if (!PasswordUtil.verify(oldPassword, customer.getPassword())) {
            System.out.println("oldPasswords do not match");
            return false;
        }
        customer.setPassword(newPassword);
        return repository.update(customer);
    }

    public boolean deactivateCustomerById(String id) {
        if (repository.findById(id) == null) {
            return false;
        }
        return repository.deactivateById(id);
    }

    public boolean activateCustomerById(String id) {
        if (repository.findById(id) == null) {
            return false;
        }
        return repository.activateById(id);
    }

    public List<Customer> getCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerById(String id) {
        return repository.findById(id);
    }

    public Customer getCustomerByEmail(String email) {
        return repository.findByEmail(email);
    }
}
