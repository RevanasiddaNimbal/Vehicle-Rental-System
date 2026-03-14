package customer.repository;

import customer.model.Customer;

import java.util.List;

public interface CustomerRepo {
    boolean save(Customer customer);

    boolean update(Customer customer);

    boolean deactivateById(String id);

    boolean activateById(String id);

    List<Customer> findAll();

    Customer findById(String id);

    Customer findByEmail(String email);
}
