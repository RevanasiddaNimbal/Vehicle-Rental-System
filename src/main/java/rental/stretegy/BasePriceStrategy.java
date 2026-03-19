package rental.stretegy;

import rental.model.Rental;

public class BasePriceStrategy implements PricingStrategy {
    @Override
    public double calculate(Rental rental) {
        long days = rental.getDays();
        return days * rental.getBasePrice();
    }
}
