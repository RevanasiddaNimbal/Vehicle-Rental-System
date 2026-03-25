package rental.billing;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RentalTimeCalculator {

    public int calculateRentalDays(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates cannot be null.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        return (int) (endDate.toEpochDay() - startDate.toEpochDay() + 1);
    }

    public long calculateLateHours(LocalDate endDate, LocalTime endTime, LocalDateTime actualReturn) {
        if (endDate == null || endTime == null || actualReturn == null) {
            throw new IllegalArgumentException("Return date/time parameters cannot be null.");
        }

        LocalDateTime expectedReturn = LocalDateTime.of(endDate, endTime);
        long totalMinutes = Duration.between(expectedReturn, actualReturn).toMinutes();

        if (totalMinutes <= 0) {
            return 0;
        }

        long totalHours = totalMinutes / 60;
        long remainderMinutes = totalMinutes % 60;

        if (remainderMinutes > 15) {
            totalHours += 1;
        }
        return totalHours;
    }
}