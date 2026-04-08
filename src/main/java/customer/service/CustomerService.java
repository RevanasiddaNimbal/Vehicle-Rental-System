package customer.service;

import customer.model.Customer;
import customer.repository.CustomerRepo;
import exception.DuplicateResourceException;
import exception.InactiveUserException;
import exception.ResourceNotFoundException;
import util.PasswordUtil;

import java.util.List;

public class CustomerService {
    private final CustomerRepo repository;

    public CustomerService(CustomerRepo repository) {
        this.repository = repository;
    }

    public boolean addCustomer(Customer customer) {
        if (repository.findByEmail(customer.getEmail()) != null) {
            throw new DuplicateResourceException("Customer with this email already exists.");
        }
        return repository.save(customer);
    }

    public boolean updateCustomer(Customer customer) {
        Customer existing = getCustomerById(customer.getId());
        if (!existing.isActive()) {
            throw new InactiveUserException("Cannot update an inactive customer.");
        }

        Customer emailOwner = repository.findByEmail(customer.getEmail());
        if (emailOwner != null && !emailOwner.getId().equals(customer.getId())) {
            throw new DuplicateResourceException("Update failed: The email is already registered to another account.");
        }

        return repository.update(customer);
    }

    public boolean deactivateCustomerById(String id) {
        Customer existing = getCustomerById(id);
        if (!existing.isActive()) return false;
        return repository.deactivateById(id);
    }

    public boolean activateCustomerById(String id) {
        Customer existing = getCustomerById(id);
        if (existing.isActive()) return false;
        return repository.activateById(id);
    }

    public List<Customer> getCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerById(String id) {
        Customer customer = repository.findById(id);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found.");
        }
        return customer;
    }

    public Customer getCustomerByEmail(String email) {
        return repository.findByEmail(email);
    }

    public boolean resetPassword(String customerId, String oldPassword, String newPassword) {
        Customer customer = getCustomerById(customerId);

        if (!PasswordUtil.verify(oldPassword, customer.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password.");
        }

        if (PasswordUtil.verify(newPassword, customer.getPassword())) {
            throw new IllegalArgumentException("New password must be different from current password.");
        }
        
        customer.setPassword(PasswordUtil.getHashPassword(newPassword));
        return repository.update(customer);
    }
}