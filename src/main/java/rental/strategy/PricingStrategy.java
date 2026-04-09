package rental.strategy;

import rental.model.Rental;

public interface PricingStrategy {
    double calculate(Rental rental);
}
