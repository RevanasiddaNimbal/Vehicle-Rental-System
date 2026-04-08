package cancellation.factory;

import cancellation.model.PolicyType;
import cancellation.strategy.CancellationStrategy;
import rental.model.Rental;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class CancellationStrategyFactory {
    private final Map<PolicyType, CancellationStrategy> strategies;

    public CancellationStrategyFactory(Map<PolicyType, CancellationStrategy> strategies) {
        this.strategies = strategies;
    }

    public CancellationStrategy getStrategy(Rental rental) {
        LocalDateTime rentalStart = rental.getStartDate().atTime(rental.getStartTime());
        long hours = Duration.between(LocalDateTime.now(), rentalStart).toHours();

        if (hours >= 48) return strategies.get(PolicyType.FULL_REFUND);
        if (hours >= 24) return strategies.get(PolicyType.PARTIAL_REFUND);
        return strategies.get(PolicyType.NO_REFUND);
    }
}
