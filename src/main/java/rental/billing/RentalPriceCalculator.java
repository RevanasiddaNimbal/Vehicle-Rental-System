package rental.billing;

import rental.model.Rental;
import rental.stretegy.PricingStrategy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class RentalPriceCalculator {
    private final Map<Integer, PricingStrategy> strategies;

    public RentalPriceCalculator(Map<Integer, PricingStrategy> strategies) {
        this.strategies = strategies;
    }

    public long getDays(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public void calculateTotalRent(Rental rental) {

        double total = 0;

        for (PricingStrategy strategy : strategies.values()) {
            total += strategy.calculate(rental);
        }

        rental.setTotalPrice(total);
    }
}
