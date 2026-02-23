package vehicle.Creator;

import util.InputUtil;
import vehicle.Models.Auto;
import vehicle.Models.Category;
import vehicle.Models.Vehicle;

import java.util.Scanner;

public class AutoCreator extends BaseCreator {
    @Override
    public Vehicle createSpecificVehicle(String id, String brand, Category category, Double price, Scanner input) {
        int capacity = InputUtil.readPositiveInt(input, "Enter Seating Capacity");
        return new Auto(id, brand, category, price, capacity);
    }
}
