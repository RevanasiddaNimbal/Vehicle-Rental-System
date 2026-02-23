package UI;

import util.InputUtil;
import vehicle.Controller.VehicleController;

import java.util.Scanner;

public class VehicleMenu implements Menu {
    private final VehicleController controller;
    private final Scanner input;

    public VehicleMenu(VehicleController controller, Scanner input) {
        this.controller = controller;
        this.input = input;
    }

    @Override
    public void show() {
        int choice;
        while (true) {
            System.out.println("\n----- VEHICLE MANAGEMENT -----");
            System.out.println("1. Add New Vehicle");
            System.out.println("2. View All Vehicles");
            System.out.println("3. Update Vehicle Details");
            System.out.println("4. Delete Vehicle");
            System.out.println("0. Back");

            choice = InputUtil.readPositiveInt(input, "Enter your choice");

            switch (choice) {
                case 1:
                    controller.addVehicle(input);
                    break;
                case 2:
                    controller.viewVehicles();
                    break;
                case 3:
                    controller.updateVehicle(input);
                    break;
                case 4:
                    controller.deleteVehicle(input);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
