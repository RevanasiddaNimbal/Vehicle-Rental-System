package user.resolver;

import customer.model.Customer;
import customer.service.CustomerService;
import exception.ResourceNotFoundException;
import user.model.UserDetails;
import user.model.UserRole;

public class CustomerUserResolver implements UserTypeResolver {
    private final CustomerService customerService;

    public CustomerUserResolver(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public UserDetails resolve(String userId) {
        Customer customer = customerService.getCustomerById(userId);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer ID " + userId + " not found.");
        }
        return new UserDetails(customer.getName(), customer.getEmail(), customer.getPhone(), UserRole.CUSTOMER);
    }
}
