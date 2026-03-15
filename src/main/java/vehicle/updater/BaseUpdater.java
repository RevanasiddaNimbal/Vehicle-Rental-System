package vehicle.updater;

import util.EnumUtil;
import util.InputUtil;
import vehicle.models.Category;
import vehicle.models.Status;
import vehicle.models.Vehicle;

import java.util.Scanner;

public abstract class BaseUpdater implements VehicleUpdater {
    @Override
    public void updateVehicle(Vehicle vehicle, Scanner input) {
        System.out.println("----Update Vehicle----");
        showCommonMenu();
        showSpecificMenu();
        handleSpecificMenu(vehicle, input);
    }

    private void showCommonMenu() {
        System.out.println("1. Update Brand");
        System.out.println("2. Update Price");
        System.out.println("3. Update Category");
        System.out.println("4. Update Status");
    }

    protected void handleCommonMenu(Vehicle vehicle, Scanner input, int choice) {

        switch (choice) {
            case 1:
                String brand = InputUtil.readString(input, "Enter new Brand");
                vehicle.setBrand(brand);
                break;
            case 2:
                double price = InputUtil.readDouble(input, "Enter new Price");
                vehicle.setPricePerDay(price);
                break;
            case 3:
                Category category = EnumUtil.selectEnum(input, Category.class, "Select new Category");
                vehicle.setCategory(category);
                break;
            case 4:
                Status status = EnumUtil.selectEnum(input, Status.class, "Select new Status");
                vehicle.setStatus(status);
                break;
            case 0:
                return;
        }
    }

    public abstract void showSpecificMenu();

    public abstract void handleSpecificMenu(Vehicle vehicle, Scanner input);

}
