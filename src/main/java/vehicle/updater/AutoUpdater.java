package vehicle.updater;

import util.InputUtil;
import vehicle.models.Auto;
import vehicle.models.Vehicle;
import vehicle.models.VehicleType;

import java.util.Scanner;

public class AutoUpdater extends BaseUpdater {
    @Override
    public VehicleType getVehicleType() {
        return VehicleType.AUTO;
    }

    @Override
    public void showSpecificMenu() {
        System.out.println("5.Update Seat Capacity");
        System.out.println("0.back");
    }

    @Override
    public void handleSpecificMenu(Vehicle vehicle, Scanner input) {
        Auto auto = (Auto) vehicle;
        int choice = InputUtil.readPositiveInt(input, "Enter your choice");
        if (choice < 5) handleCommonMenu(vehicle, input, choice);
        else {
            switch (choice) {
                case 5:
                    int capacity = InputUtil.readPositiveInt(input, "Enter new Seat Capacity");
                    auto.setSeatingCapacity(capacity);
                    break;
                default:
                    System.out.println("Invalid choice.Please try again");
            }
        }
    }
}
