package UI;

import vehicleowner.models.VehicleOwner;

import java.util.List;

public class OwnersPrinter implements UserPrinter<VehicleOwner> {
    @Override
    public void print(List<VehicleOwner> owners) {
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.printf(
                "%-12s %-15s %-25s %-15s %-26s %-8s%n",
                "ID", "Name", "Email", "Phone", "Address", "Active"
        );
        System.out.println("---------------------------------------------------------------------------------------------------------------");

        for (VehicleOwner owner : owners) {
            System.out.printf(
                    "%-12s %-15s %-25s %-15s %-26s %-8s%n",
                    owner.getId(),
                    owner.getName(),
                    owner.getEmail(),
                    owner.getPhone(),
                    owner.getAddress(),
                    owner.isActive() ? "Yes" : "No"
            );
        }

        System.out.println("---------------------------------------------------------------------------------------------------------------");
    }

}
