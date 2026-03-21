package cancellation.stretegy;

import cancellation.model.PolicyType;
import rental.model.Rental;

import java.time.Duration;
import java.time.LocalDateTime;

public class PartialRefundStrategy implements CancellationStrategy {
    @Override
    public double calculateRefund(Rental rental) {
        long hours = Duration.between(
                LocalDateTime.now(),
                rental.getStartDate().atTime(rental.getStartTime())
        ).toHours();

        if (hours >= 48) return rental.getTotalPrice() * 0.8;
        if (hours >= 24) return rental.getTotalPrice() * 0.5;
        return 0;
    }

    @Override
    public PolicyType getPolicyName() {
        return PolicyType.PARTIAL_REFUND;
    }
}
