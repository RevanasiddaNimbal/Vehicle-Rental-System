package UI;

import util.InputUtil;
import vehicleowner.controller.VehicleOwnerController;

import java.util.Scanner;

public class VehicleOwnerAccountMenu implements UsersMenu {
    private final VehicleOwnerController ownerController;
    private final Scanner input;

    public VehicleOwnerAccountMenu(Scanner input, VehicleOwnerController ownerController) {
        this.input = input;
        this.ownerController = ownerController;
    }

    @Override
    public void show(String ownerId) {
        int choice;
        while (true) {
            try {
                System.out.println("\n========= Vehicle Owner Account Management Menu =============");
                System.out.println("1. Reset Password");
                System.out.println("2. Update My Account ");
                System.out.println("0. back");
                choice = InputUtil.readPositiveInt(input, "Enter your choice");
                switch (choice) {
                    case 1:
                        ownerController.resetPassword(input, ownerId);
                        break;
                    case 2:
                        ownerController.updateVehicleOwner(input, ownerId);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice.Please try again");
                }
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}
