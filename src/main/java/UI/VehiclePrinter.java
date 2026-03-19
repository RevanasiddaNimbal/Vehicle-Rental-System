package UI;

import vehicle.models.Vehicle;

import java.util.List;

import static util.OutputUtil.valueOrDash;

public class VehiclePrinter implements UserPrinter<Vehicle> {
    @Override
    public void print(List<Vehicle> vehicles) {
        System.out.println("-----------------------------------------------------------------------------------------------");
        System.out.printf(
                "%-10s %-15s %-12s %-10s %-12s %-10s %-8s %-12s%n",
                "ID", "Brand", "Category", "Price", "Status",
                "Engine CC", "Seats", "Fuel"
        );
        System.out.println("-----------------------------------------------------------------------------------------------");

        for (Vehicle v : vehicles) {
            System.out.printf(
                    "%-10s %-15s %-12s ₹%-9.2f %-12s %-11s %-8s %-12s%n",
                    v.getId(),
                    v.getBrand(),
                    v.getCategory(),
                    v.getPricePerDay(),
                    v.getStatus(),
                    valueOrDash(v.getEnginCapacity()),
                    valueOrDash(v.getSeatingCapacity()),
                    valueOrDash(v.getFuelType())
            );
        }

        System.out.println("-----------------------------------------------------------------------------------------------");

    }
}
