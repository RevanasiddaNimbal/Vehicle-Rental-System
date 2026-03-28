package invoice.model;

import customer.model.Customer;
import rental.model.Rental;

import java.util.List;

public class Invoice {
    private final Customer customer;
    private final List<Rental> rentals;
    private double totalBasePrice;
    private double totalWeekendPrice;
    private double totalDiscountPrice;
    private double totalNetPrice;
    private double totalSecurityDeposit;

    public Invoice(Customer customer, List<Rental> rentals) {
        this.customer = customer;
        this.rentals = rentals;
        calculateTotalRents();
    }

    private void calculateTotalRents() {
        totalBasePrice = 0;
        totalWeekendPrice = 0;
        totalDiscountPrice = 0;
        totalNetPrice = 0;
        totalSecurityDeposit = 0;

        if (rentals != null) {
            for (Rental rental : rentals) {
                totalBasePrice += rental.getBasePrice();
                totalWeekendPrice += rental.getWeekendCharge();
                totalDiscountPrice += rental.getDiscount();
                totalNetPrice += rental.getTotalPrice();
                totalSecurityDeposit += rental.getSecurityDeposit();
            }
        }
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTotalBasePrice() {
        return totalBasePrice;
    }

    public double getTotalWeekendPrice() {
        return totalWeekendPrice;
    }

    public double getTotalDiscountPrice() {
        return totalDiscountPrice;
    }

    public double getTotalNetPrice() {
        return totalNetPrice;
    }

    public double getTotalSecurityDeposit() {
        return totalSecurityDeposit;
    }

    public double getGrandTotalPayable() {
        return totalNetPrice + totalSecurityDeposit;
    }
}