package penalty.strategy;

import rental.model.Rental;

public interface PenaltyStrategy {
    double calculate(Rental rental);
}
