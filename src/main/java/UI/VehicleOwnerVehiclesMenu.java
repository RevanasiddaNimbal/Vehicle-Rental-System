package UI;

import util.InputUtil;
import vehicle.controller.VehicleController;

import java.util.Scanner;

public class VehicleOwnerVehiclesMenu implements UsersMenu {
    private final VehicleController vehicleController;
    private final Scanner input;

    public VehicleOwnerVehiclesMenu(Scanner input, VehicleController vehicleController) {
        this.input = input;
        this.vehicleController = vehicleController;
    }

    @Override
    public void show(String ownerId) {
        int choice;
        while (true) {
            System.out.println("\n========= Vehicles Management Menu =============");
            System.out.println("1. Add New Vehicle");
            System.out.println("2. View My Vehicles");
            System.out.println("3. Update My Vehicle");
            System.out.println("4. Delete My Vehicle");
            System.out.println("0. back");
            choice = InputUtil.readPositiveInt(input, "Enter your choice");
            switch (choice) {
                case 1:
                    vehicleController.addVehicle(input, ownerId);
                    break;
                case 2:
                    vehicleController.viewVehicleByOwnerId(ownerId);
                    break;
                case 3:
                    vehicleController.updateVehicle(input, ownerId);
                    break;
                case 4:
                    vehicleController.deleteVehicle(input, ownerId);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
