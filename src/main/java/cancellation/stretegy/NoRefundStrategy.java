package cancellation.stretegy;

import cancellation.model.PolicyType;
import rental.model.Rental;

public class NoRefundStrategy implements CancellationStrategy {
    @Override
    public double calculateRefund(Rental rental) {
        return 0;
    }

    @Override
    public PolicyType getPolicyName() {
        return PolicyType.NO_REFUND;
    }
}
