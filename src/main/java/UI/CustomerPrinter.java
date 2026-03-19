package UI;

import customer.model.Customer;

import java.util.List;

public class CustomerPrinter implements UserPrinter<Customer> {
    @Override
    public void print(List<Customer> customers) {
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
        System.out.printf(
                "%-12s %-15s %-25s %-15s %-20s %-20s %-8s%n",
                "ID", "Name", "Email", "Phone", "Address", "License No", "Active"
        );
        System.out.println("---------------------------------------------------------------------------------------------------------------------");

        for (Customer customer : customers) {
            System.out.printf(
                    "%-12s %-15s %-25s %-15s %-20s %-20s %-8s%n",
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getAddress(),
                    customer.getDrivingLicenseNumber(),
                    customer.isActive() ? "Yes" : "No"
            );
        }

        System.out.println("---------------------------------------------------------------------------------------------------------------------");
    }
}
