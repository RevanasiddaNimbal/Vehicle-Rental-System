package UI;

import rental.model.Rental;

import java.util.List;

public class RentalPrinter implements UserPrinter<Rental> {
    @Override
    public void print(List<Rental> rentals) {
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("%-8s %-12s %-12s %-12s %-12s %-10s%n",
                "ID", "Customer ID", "Vehicle ID", "Start Date", "End Date", "Status");
        System.out.println("-----------------------------------------------------------------------------------");
        for (Rental r : rentals) {
            System.out.printf("%-8d %-12s %-12s %-12s %-12s %-10s%n",
                    r.getId(),
                    r.getCustomerId(),
                    r.getVehicleId(),
                    r.getStartDate(),
                    r.getEndDate(),
                    r.getStatus());
        }
        System.out.println("-----------------------------------------------------------------------------------");
    }
}
