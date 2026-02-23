package vehicle.Updater;

import util.EnumUtil;
import util.InputUtil;
import vehicle.Models.Category;
import vehicle.Models.Status;
import vehicle.Models.Vehicle;

import java.util.Scanner;

public abstract class BaseUpdater implements VehicleUpdater {
    @Override
    public boolean updateVehicle(Vehicle vehicle, Scanner input) {
        System.out.println("----Update Vehicle----");
        showCommonMenu();
        showSpecificMenu();
        return handleSpecificMenu(vehicle, input);
    }

    private void showCommonMenu() {
        System.out.println("1. Update Brand");
        System.out.println("2. Update Price");
        System.out.println("3. Update Category");
        System.out.println("4. Update Status");
    }

    protected boolean handleCommonMenu(Vehicle vehicle, Scanner input, int choice) {

        switch (choice) {
            case 1:
                String brand = InputUtil.readString(input, "Enter new Brand");
                vehicle.setBrand(brand);
                return true;
            case 2:
                double price = InputUtil.readDouble(input, "Enter new Price");
                vehicle.setPricePerDay(price);
                return true;
            case 3:
                Category category = EnumUtil.selectEnum(input, Category.class, "Select new Category");
                vehicle.setCategory(category);
                return true;
            case 4:
                Status status = EnumUtil.selectEnum(input, Status.class, "Select new Status");
                vehicle.setStatus(status);
                return true;
            case 0:
                return false;
        }
        return false;
    }

    public abstract void showSpecificMenu();

    public abstract boolean handleSpecificMenu(Vehicle vehicle, Scanner input);

}
