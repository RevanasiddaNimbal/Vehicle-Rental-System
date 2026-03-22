package UI;

import util.InputUtil;
import vehicle.controller.VehicleController;

import java.util.Scanner;

public class AdminsVehicleMenu implements UsersMenu {
    private final VehicleController vehicleController;
    private final Scanner input;

    public AdminsVehicleMenu(Scanner input, VehicleController vehicleController) {
        this.input = input;
        this.vehicleController = vehicleController;
    }

    @Override
    public void show(String adminId) {
        int choice;
        while (true) {
            System.out.println("\n========= Vehicle Management Menu =========");
            System.out.println("1. View All Vehicles");
            System.out.println("0. back");
            choice = InputUtil.readPositiveInt(input, "Enter your choice");
            switch (choice) {
                case 1:
                    vehicleController.viewVehicles();
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice");
            }

        }
    }
}
