package UI;

import cancellation.model.CancellationRecord;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CancellationPrinter implements UserPrinter<CancellationRecord> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

    @Override
    public void print(List<CancellationRecord> records) {

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf(
                "%-12s %-10s %-12s %-12s %-12s %-18s %-12s %-20s%n",
                "Cancel ID", "Rental ID", "Vehicle ID", "Customer ID", "Owner ID",
                "Policy", "Refund", "Canceled At"
        );
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");

        for (CancellationRecord record : records) {
            String formattedTime = record.getCanceledAt().format(FORMATTER);
            System.out.printf(
                    "%-12s %-10d %-12s %-12s %-12s %-18s %-12.2f %-20s%n",
                    record.getId(),
                    record.getRentalId(),
                    record.getVehicleId(),
                    record.getCustomerId(),
                    record.getOwnerId(),
                    record.getPolicyApplied(),
                    record.getRefundAmount(),
                    formattedTime
            );
        }

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
    }

}
