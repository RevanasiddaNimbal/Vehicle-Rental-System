package vehicle.Updater;

import util.EnumUtil;
import util.InputUtil;
import vehicle.Models.Bike;
import vehicle.Models.FuelType;
import vehicle.Models.Vehicle;

import java.util.Scanner;

public class BikeUpdater extends BaseUpdater {
    @Override
    public void showSpecificMenu() {
        System.out.println("5. Update engine Capacity");
        System.out.println("6. Update fuel Type");
        System.out.println("0. Back");
    }

    @Override
    public void handleSpecificMenu(Vehicle vehicle, Scanner input) {
        Bike bike = (Bike) vehicle;
        int choice = InputUtil.readPositiveInt(input, "Enter your choice");
        if (choice < 5) handleCommonMenu(vehicle, input, choice);
        else {
            switch (choice) {
                case 5:
                    int capacity = InputUtil.readPositiveInt(input, "Enter new engine capacity");
                    bike.setEnginCapacity(capacity);
                    break;
                case 6:
                    FuelType fuel = EnumUtil.selectEnum(input, FuelType.class, "Select new fuel type");
                    bike.setFuel(fuel);
                    break;
                default:
                    System.out.println("Invalid choice.Please try again");

            }
        }
    }
}
