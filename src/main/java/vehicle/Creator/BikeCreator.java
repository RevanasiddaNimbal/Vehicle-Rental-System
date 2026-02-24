package vehicle.Creator;

import util.EnumUtil;
import util.InputUtil;
import vehicle.Models.Bike;
import vehicle.Models.Category;
import vehicle.Models.FuelType;
import vehicle.Models.Vehicle;

import java.util.Scanner;

public class BikeCreator extends BaseCreator {
    @Override
    public Vehicle createSpecificVehicle(String id, String brand, Category category, Double price, Scanner input) {
        int capacity = InputUtil.readPositiveInt(input, "Enter Engine Capacity");
        FuelType fuel = EnumUtil.selectEnum(input, FuelType.class, "Select Fuel Type");
        return new Bike(id, brand, category, price, fuel, capacity);
    }
}
