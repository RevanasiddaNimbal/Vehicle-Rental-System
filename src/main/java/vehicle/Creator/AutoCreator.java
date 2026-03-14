package vehicle.Creator;

import util.InputUtil;
import vehicle.Models.Auto;
import vehicle.Models.Category;
import vehicle.Models.Status;
import vehicle.Models.Vehicle;

import java.util.Scanner;

public class AutoCreator extends BaseCreator {
    @Override
    public Vehicle createSpecificVehicle(String id, String brand, Category category, Double price, Status status, Scanner input, String ownerId) {
        int capacity = InputUtil.readPositiveInt(input, "Enter Seating Capacity");
        return new Auto(id, brand, category, price, capacity, status, ownerId);
    }
}
