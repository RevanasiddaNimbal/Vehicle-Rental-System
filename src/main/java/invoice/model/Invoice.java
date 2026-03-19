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

    public Invoice(Customer customer, List<Rental> rentals) {
        this.customer = customer;
        this.rentals = rentals;
    }

    public void calculateTotalRents() {
        for (Rental rental : rentals) {
            totalBasePrice += rental.getBasePrice();
            totalWeekendPrice += rental.getWeekendCharge();
            totalDiscountPrice += rental.getDiscount();
            totalNetPrice += rental.getTotalPrice();
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


}
