package vehicle.Creator;

import util.EnumUtil;
import util.InputUtil;
import vehicle.Models.*;

import java.util.Scanner;

public class BikeCreator extends BaseCreator {
    @Override
    public Vehicle createSpecificVehicle(String id, String brand, Category category, Double price, Status status, Scanner input, String ownerId) {
        int capacity = InputUtil.readPositiveInt(input, "Enter Engine Capacity");
        FuelType fuel = EnumUtil.selectEnum(input, FuelType.class, "Select Fuel Type");
        return new Bike(id, brand, category, price, fuel, capacity, status, ownerId);
    }
}
