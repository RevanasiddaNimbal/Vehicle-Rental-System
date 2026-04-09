package rental.billing;

import rental.model.Rental;
import rental.strategy.PricingStrategy;
import rental.strategy.SecurityDepositStrategy;

import java.util.Map;

public class RentalPriceCalculator {
    private final Map<Integer, PricingStrategy> strategies;
    private final SecurityDepositStrategy depositStrategy;

    public RentalPriceCalculator(Map<Integer, PricingStrategy> strategies, SecurityDepositStrategy depositStrategy) {
        if (strategies == null || strategies.isEmpty() || depositStrategy == null) {
            throw new IllegalArgumentException("Pricing strategies and depositStrategy  cannot be null or empty.");
        }
        this.strategies = strategies;
        this.depositStrategy = depositStrategy;
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

    public void calculateTotalDeposit(Rental rental, String type) {
        if (rental == null || type == null) {
            throw new IllegalArgumentException("Rental and vehicle type cannot be null for price calculation.");
        }
        double secureDeposit = depositStrategy.calculateDeposit(type, rental.getTotalPrice());
        if (secureDeposit < 0) {
            secureDeposit = 0;
        }
        rental.setSecurityDeposit(secureDeposit);
    }
}