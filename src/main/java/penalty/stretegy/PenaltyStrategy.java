package penalty.stretegy;

import penalty.model.Penalty;
import rental.model.Rental;

public interface PenaltyStrategy {
    double calculate(Rental rental, Penalty penalty);
}
