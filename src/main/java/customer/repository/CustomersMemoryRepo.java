package customer.repository;

import customer.model.Customer;
import exception.DuplicateResourceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomersMemoryRepo implements CustomerRepo {
    private final Map<String, Customer> Storage = new HashMap<>();

    @Override
    public boolean save(Customer customer) {
        if (Storage.containsKey(customer.getId())) {
            return false;
        }
        
        if (findByEmail(customer.getEmail()) != null) {
            throw new DuplicateResourceException("Database rejected registration: Email already exists.");
        }

        Storage.put(customer.getId(), customer);
        return true;
    }

    @Override
    public boolean update(Customer customer) {
        if (!Storage.containsKey(customer.getId())) {
            return false;
        }
        Storage.put(customer.getId(), customer);
        return true;
    }

    @Override
    public boolean deactivateById(String id) {
        Customer customer = Storage.get(id);
        if (customer != null && customer.isActive()) {
            customer.deactivate();
            Storage.put(id, customer);
            return true;
        }
        return false;
    }

    @Override
    public boolean activateById(String id) {
        Customer customer = Storage.get(id);
        if (customer != null && !customer.isActive()) {
            customer.activate();
            Storage.put(id, customer);
            return true;
        }
        return false;
    }

    @Override
    public Customer findById(String id) {
        return Storage.get(id);
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(Storage.values());
    }

    @Override
    public Customer findByEmail(String email) {
        for (Customer customer : Storage.values()) {
            if (customer.getEmail().equalsIgnoreCase(email) && customer.isActive()) {
                return customer;
            }
        }
        return null;
    }
}