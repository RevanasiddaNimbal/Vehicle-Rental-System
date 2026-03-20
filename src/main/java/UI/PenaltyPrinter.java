package UI;

import penalty.model.Penalty;

import java.util.List;

public class PenaltyPrinter implements UserPrinter<Penalty> {

    @Override
    public void print(List<Penalty> penalties) {
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
        System.out.printf(
                "%-12s %-10s %-12s %-12s %-10s %-20s %-20s %-12s%n",
                "Penalty ID", "Rental ID", "Vehicle ID", "Customer ID", "Amount", "Type", "Reason", "Issued Date"
        );
        System.out.println("---------------------------------------------------------------------------------------------------------------------");

        for (Penalty penalty : penalties) {
            System.out.printf(
                    "%-12s %-10d %-12s %-12s %-10.2f %-20s %-20s %-12s%n",
                    penalty.getId(),
                    penalty.getRentalId(),
                    penalty.getVehicleId(),
                    penalty.getCustomerId(),
                    penalty.getAmount(),
                    penalty.getType(),
                    penalty.getReason(),
                    penalty.getIssuedDate()
            );
        }

        System.out.println("---------------------------------------------------------------------------------------------------------------------");
    }
}