package vehicle.Updater;

import util.InputUtil;
import vehicle.Models.Auto;
import vehicle.Models.Vehicle;

import java.util.Scanner;

public class AutoUpdater extends BaseUpdater {
    @Override
    public void showSpecificMenu() {
        System.out.println("5.Update Seat Capacity");
        System.out.println("0.back");
    }

    @Override
    public boolean handleSpecificMenu(Vehicle vehicle, Scanner input) {
        Auto auto = (Auto) vehicle;
        int choice = InputUtil.readPositiveInt(input, "Enter your choice");
        if (choice < 5) return handleCommonMenu(vehicle, input, choice);
        else {
            switch (choice) {
                case 5:
                    int capacity = InputUtil.readPositiveInt(input, "Enter new Seat Capacity");
                    auto.setSeatingCapacity(capacity);
                    return true;
                default:
                    System.out.println("Invalid choice.Please try again");
                    return false;
            }
        }
    }
}
