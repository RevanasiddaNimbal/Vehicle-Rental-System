package penalty.factory;

import penalty.model.PenaltyType;
import penalty.stretegy.LateReturnPenaltyStrategy;
import penalty.stretegy.PenaltyStrategy;
import rental.billing.RentalTimeCalculator;

public class PenaltyStrategyFactory {
    private final RentalTimeCalculator rentalTimeCalculator;

    public PenaltyStrategyFactory(RentalTimeCalculator rentalTimeCalculator) {
        this.rentalTimeCalculator = rentalTimeCalculator;
    }

    public PenaltyStrategy getPenaltyStrategy(PenaltyType penaltyType) {
        switch (penaltyType) {
            case LATE_RETURN:
                return new LateReturnPenaltyStrategy(rentalTimeCalculator);
            default:
                return null;
        }
    }
}
