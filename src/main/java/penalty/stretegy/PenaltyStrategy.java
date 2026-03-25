package penalty.stretegy;

import rental.model.Rental;

public interface PenaltyStrategy {
    double calculate(Rental rental);
}
