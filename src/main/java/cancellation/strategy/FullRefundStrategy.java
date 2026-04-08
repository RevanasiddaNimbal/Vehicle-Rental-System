package cancellation.strategy;

import cancellation.model.PolicyType;
import rental.model.Rental;

public class FullRefundStrategy implements CancellationStrategy {
    @Override
    public PolicyType getPolicyName() {
        return PolicyType.FULL_REFUND;
    }

    @Override
    public double calculateRefund(Rental rental) {
        return rental.getTotalPrice();
    }
}
