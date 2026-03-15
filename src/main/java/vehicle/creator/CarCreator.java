package vehicle.creator;

import util.EnumUtil;
import util.InputUtil;
import vehicle.models.*;

import java.util.Scanner;

public class CarCreator extends BaseCreator {
    @Override
    public Vehicle createSpecificVehicle(String id, String brand, Category category, Double price, Status status, Scanner input, String ownerId) {
        int capacity = InputUtil.readPositiveInt(input, "Enter seating Capacity");
        FuelType fuel = EnumUtil.selectEnum(input, FuelType.class, "Select Fuel Type");
        return new Car(id, brand, category, price, capacity, fuel, status, ownerId);
    }
}
