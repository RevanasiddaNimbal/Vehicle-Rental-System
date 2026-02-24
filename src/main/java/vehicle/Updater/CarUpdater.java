package vehicle.Updater;

import util.EnumUtil;
import util.InputUtil;
import vehicle.Models.Car;
import vehicle.Models.FuelType;
import vehicle.Models.Vehicle;

import java.util.Scanner;

public class CarUpdater extends BaseUpdater {
    @Override
    public void showSpecificMenu() {
        System.out.println("5. Update Seat Capacity");
        System.out.println("6. Update Fuel Type");
        System.out.println("0. back");
    }

    @Override
    public boolean handleSpecificMenu(Vehicle vehicle, Scanner input) {
        Car car = (Car) vehicle;
        int choice = InputUtil.readPositiveInt(input, "Enter Your choice");
        if (choice < 5) return handleCommonMenu(vehicle, input, choice);
        else {
            switch (choice) {
                case 5:
                    int capacity = InputUtil.readPositiveInt(input, "Enter new seat capacity");
                    car.setSeatingCapacity(capacity);
                    return true;
                case 6:
                    FuelType fuel = EnumUtil.selectEnum(input, FuelType.class, "Select new Fuel Type");
                    car.setFuel(fuel);
                    return true;
                default:
                    System.out.println("Invalid choice.Please try again");
                    return false;
            }
        }

    }
}
