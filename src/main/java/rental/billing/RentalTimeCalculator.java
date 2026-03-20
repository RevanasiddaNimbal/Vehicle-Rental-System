package rental.billing;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RentalTimeCalculator {
    public int calculateRentalDays(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) return 0;
        return (int) (endDate.toEpochDay() - startDate.toEpochDay() + 1);
    }

    public long calculateLateHours(LocalDate endDate, LocalTime endTime, LocalDateTime actualReturn) {
        LocalDateTime expectedReturn = LocalDateTime.of(endDate, endTime);
        long hours = Duration.between(expectedReturn, actualReturn).toHours();
        return Math.max(hours, 0);
    }
}
