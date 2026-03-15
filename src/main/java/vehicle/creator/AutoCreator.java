package vehicle.creator;

import util.InputUtil;
import vehicle.models.Auto;
import vehicle.models.Category;
import vehicle.models.Status;
import vehicle.models.Vehicle;

import java.util.Scanner;

public class AutoCreator extends BaseCreator {
    @Override
    public Vehicle createSpecificVehicle(String id, String brand, Category category, Double price, Status status, Scanner input, String ownerId) {
        int capacity = InputUtil.readPositiveInt(input, "Enter Seating Capacity");
        return new Auto(id, brand, category, price, capacity, status, ownerId);
    }
}
