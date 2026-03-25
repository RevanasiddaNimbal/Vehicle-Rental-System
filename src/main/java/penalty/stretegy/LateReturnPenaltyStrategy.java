package penalty.stretegy;

import rental.billing.RentalTimeCalculator;
import rental.model.Rental;

import java.time.LocalDateTime;

public class LateReturnPenaltyStrategy implements PenaltyStrategy {
    private static final double PENALTY_PERCENTAGE = 0.03;
    private final RentalTimeCalculator timeCalculator;

    public LateReturnPenaltyStrategy(RentalTimeCalculator timeCalculator) {
        this.timeCalculator = timeCalculator;
    }

    @Override
    public double calculate(Rental rental) {
        long lateHours = timeCalculator.calculateLateHours(
                rental.getEndDate(),
                rental.getEndTime(),
                LocalDateTime.now()
        );

        if (lateHours <= 0) {
            return 0.0;
        }

        return rental.getTotalPrice() * PENALTY_PERCENTAGE * lateHours;
    }
}