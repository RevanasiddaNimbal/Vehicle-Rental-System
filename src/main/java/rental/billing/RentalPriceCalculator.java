package rental.billing;

import rental.model.Rental;
import rental.stretegy.PricingStrategy;

import java.util.Map;

public class RentalPriceCalculator {
    private final Map<Integer, PricingStrategy> strategies;

    public RentalPriceCalculator(Map<Integer, PricingStrategy> strategies) {
        if (strategies == null || strategies.isEmpty()) {
            throw new IllegalArgumentException("Pricing strategies cannot be null or empty.");
        }
        this.strategies = strategies;
    }

    public void calculateTotalRent(Rental rental) {
        if (rental == null) {
            throw new IllegalArgumentException("Rental cannot be null for price calculation.");
        }

        double total = 0;

        for (PricingStrategy strategy : strategies.values()) {
            total += strategy.calculate(rental);
        }

        if (total < 0) {
            total = 0;
        }

        rental.setTotalPrice(total);
    }
}