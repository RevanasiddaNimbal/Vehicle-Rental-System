package rental.stretegy;

import rental.model.Rental;

public interface PricingStrategy {
    double calculate(Rental rental);
}
