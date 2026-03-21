package cancellation.stretegy;

import cancellation.model.PolicyType;
import rental.model.Rental;

public interface CancellationStrategy {
    double calculateRefund(Rental rental);

    PolicyType getPolicyName();
}
