package cancellation.strategy;

import cancellation.model.PolicyType;
import rental.model.Rental;

public class PartialRefundStrategy implements CancellationStrategy {
    @Override
    public double calculateRefund(Rental rental) {
        return rental.getTotalPrice() * 0.5;
    }

    @Override
    public PolicyType getPolicyName() {
        return PolicyType.PARTIAL_REFUND;
    }
}
