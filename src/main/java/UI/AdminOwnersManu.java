package UI;

import util.InputUtil;
import vehicleowner.controller.VehicleOwnerController;

import java.util.Scanner;

public class AdminOwnersManu implements UsersMenu {
    private final VehicleOwnerController ownerController;
    private final Scanner input;

    public AdminOwnersManu(Scanner input, VehicleOwnerController vehicleController) {
        this.input = input;
        this.ownerController = vehicleController;
    }

    @Override
    public void show(String adminId) {
        int choice;
        while (true) {
            System.out.println("======== Vehicle Owners Management Menu =========");
            System.out.println("1.  View All Vehicle Owners");
            System.out.println("2.  Activate  Vehicle Owner");
            System.out.println("3.  Deactivate Vehicle Owner");
            System.out.println("0. Back");
            choice = InputUtil.readPositiveInt(input, "Enter your choice");
            switch (choice) {
                case 1:
                    ownerController.viewOwners();
                    break;
                case 2:
                    ownerController.activateVehicleOwner(input);
                    break;
                case 3:
                    ownerController.deactivateVehicleOwner(input);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice");

            }
        }
    }
}
