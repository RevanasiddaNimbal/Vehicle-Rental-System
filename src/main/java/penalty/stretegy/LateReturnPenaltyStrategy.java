package penalty.stretegy;

import penalty.model.Penalty;
import rental.billing.RentalTimeCalculator;
import rental.model.Rental;

public class LateReturnPenaltyStrategy implements PenaltyStrategy {
    private static final double PENALTY_PERCENTAGE = 0.03;
    private final RentalTimeCalculator timeCalculator;

    public LateReturnPenaltyStrategy(RentalTimeCalculator timeCalculator) {
        this.timeCalculator = timeCalculator;
    }

    @Override
    public double calculate(Rental rental, Penalty penalty) {
        long lateHours = timeCalculator.calculateLateHours(
                rental.getEndDate(),
                rental.getEndTime(),
                java.time.LocalDateTime.now() // actual return time
        );

        if (lateHours <= 0) return 0;

        double totalFine = rental.getTotalPrice() * PENALTY_PERCENTAGE * lateHours;
        penalty.setPenaltyAmount(totalFine);
        return totalFine;
    }
}