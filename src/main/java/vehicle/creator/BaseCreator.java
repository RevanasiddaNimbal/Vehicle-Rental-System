package vehicle.creator;

import util.EnumUtil;
import util.InputUtil;
import vehicle.models.Category;
import vehicle.models.Status;
import vehicle.models.Vehicle;
import vehicle.models.VehicleType;
import vehicle.policy.CategoryPolicy;

import java.util.Scanner;

public abstract class BaseCreator implements VehicleCreator {
    protected abstract VehicleType getVehicleType();

    @Override
    public Vehicle createVehicle(String id, Scanner input, String ownerId) {
        String brand = InputUtil.readString(input, "Enter Brand");
        Category category = EnumUtil.selectCategory(input, CategoryPolicy.allowedCategories(getVehicleType()), "Select Category");
        Double price = InputUtil.readDouble(input, "Enter Price");
        Status status = EnumUtil.selectEnum(input, Status.class, "Select Status");
        return createSpecificVehicle(id, brand, category, price, status, input, ownerId);
    }

    public abstract Vehicle createSpecificVehicle(String id, String brand, Category category, Double price, Status status, Scanner input, String ownerId);
}
