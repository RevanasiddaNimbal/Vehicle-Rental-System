package vehicle.creator;

import util.InputUtil;
import vehicle.models.*;

import java.util.Scanner;

public class AutoCreator extends BaseCreator {
    @Override
    public VehicleType getVehicleType() {
        return VehicleType.AUTO;
    }

    @Override
    public Vehicle createSpecificVehicle(String id, String brand, Category category, Double price, Status status, Scanner input, String ownerId) {
        int capacity = InputUtil.readPositiveInt(input, "Enter Seating Capacity");
        return new Auto(id, brand, category, price, capacity, status, ownerId);
    }
}
