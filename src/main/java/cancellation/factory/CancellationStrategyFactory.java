package cancellation.factory;

import cancellation.stretegy.CancellationStrategy;
import cancellation.stretegy.FullRefundStrategy;
import cancellation.stretegy.NoRefundStrategy;
import cancellation.stretegy.PartialRefundStrategy;
import rental.model.Rental;

import java.time.Duration;
import java.time.LocalDateTime;

public class CancellationStrategyFactory {
    public static CancellationStrategy getStrategy(Rental rental) {
        LocalDateTime rentalStart = rental.getStartDate().atTime(rental.getStartTime());
        long hours = Duration.between(LocalDateTime.now(), rentalStart).toHours();

        if (hours >= 48) return new FullRefundStrategy();
        if (hours >= 24) return new PartialRefundStrategy();
        return new NoRefundStrategy();
    }
}
