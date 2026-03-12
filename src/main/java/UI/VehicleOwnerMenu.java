package UI;

import authentication.model.UserRole;
import util.InputUtil;
import vehicle.Controller.VehicleController;
import vehicleowner.Controller.VehicleOwnerController;

import java.util.Scanner;

public class VehicleOwnerMenu implements Menu {
    private final Scanner input;
    private final VehicleController vehicleController;
    private final VehicleOwnerController ownerController;

    public VehicleOwnerMenu(Scanner input, VehicleController vehicleController, VehicleOwnerController ownerController) {
        this.input = input;
        this.vehicleController = vehicleController;
        this.ownerController = ownerController;
    }

    @Override
    public void show(UserRole role) {
        int choice;
        while (true) {
            System.out.println("\n========= VEHICLE OWNER PANEL =============");
            System.out.println("1. Add New Vehicle");
            System.out.println("2. View My Vehicles");
            System.out.println("3. Update My Vehicle");
            System.out.println("4. Delete My Vehicle");
            System.out.println("5. View Active Rentals on My Vehicles");
            System.out.println("6. View Rental History of My Vehicles");
            System.out.println("7. Update My Account");
            System.out.println("0. Logout");

            choice = InputUtil.readPositiveInt(input, "Enter your choice");

            switch (choice) {
                case 1:
                    vehicleController.addVehicle(input);
                    break;
                case 2:
                    break;
                case 3:
                    vehicleController.updateVehicle(input);
                    break;
                case 4:
                    vehicleController.deleteVehicle(input);
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    ownerController.updateVehicleOwner(input);
                    break;
                case 0:
                    System.out.println("Logged out from Owner panel.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
