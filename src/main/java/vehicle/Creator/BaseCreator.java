package vehicle.Creator;

import util.EnumUtil;
import util.InputUtil;
import vehicle.Models.Category;
import vehicle.Models.Status;
import vehicle.Models.Vehicle;

import java.util.Scanner;

public abstract class BaseCreator implements VehicleCreator {
    @Override
    public Vehicle createVehicle(String id, Scanner input) {
        String brand = InputUtil.readString(input, "Enter Brand");
        Category category = EnumUtil.selectEnum(input, Category.class, "Select Vehicle Category");
        Double price = InputUtil.readDouble(input, "Enter Price");
        Status status = EnumUtil.selectEnum(input, Status.class, "Select Status");
        return createSpecificVehicle(id, brand, category, price, status, input);
    }

    public abstract Vehicle createSpecificVehicle(String id, String brand, Category category, Double price, Status status, Scanner input);
}
