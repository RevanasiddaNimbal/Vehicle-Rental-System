package UI;

import authentication.model.UserRole;
import rental.controller.RentalController;
import util.InputUtil;
import vehicle.controller.VehicleController;
import vehicleowner.controller.VehicleOwnerController;

import java.util.Scanner;

public class VehicleOwnerMenu implements UsersMenu {
    private final Scanner input;
    private final VehicleController vehicleController;
    private final VehicleOwnerController ownerController;
    private final RentalController rentalController;

    public VehicleOwnerMenu(Scanner input, VehicleController vehicleController, VehicleOwnerController ownerController, RentalController rentalController) {
        this.input = input;
        this.vehicleController = vehicleController;
        this.ownerController = ownerController;
        this.rentalController = rentalController;
    }

    @Override
    public void show(UserRole role, String ownerId) {
        int choice;
        while (true) {
            System.out.println("\n========= VEHICLE OWNER PANEL =============");
            System.out.println("1. Add New Vehicle");
            System.out.println("2. View My Vehicles");
            System.out.println("3. Update My Vehicle");
            System.out.println("4. Delete My Vehicle");
            System.out.println("5. View Active Rentals on My Vehicles");
            System.out.println("6. View Rental History of My Vehicles");
            System.out.println("7. View My Active Customers");
            System.out.println("8. View My All Customers");
            System.out.println("9. Update My Account");
            System.out.println("0. Logout");

            choice = InputUtil.readPositiveInt(input, "Enter your choice");

            switch (choice) {
                case 1:
                    vehicleController.addVehicle(input, ownerId);
                    break;
                case 2:
                    vehicleController.viewVehicleByOwnerId(ownerId);
                    break;
                case 3:
                    vehicleController.updateVehicle(input);
                    break;
                case 4:
                    vehicleController.deleteVehicle(input);
                    break;
                case 5:
                    rentalController.viewActiveRentalsByOwnerId(ownerId);
                    break;
                case 6:
                    rentalController.viewRentalsByOwnerId(ownerId);
                    break;
                case 7:
                    rentalController.viewActiveCustomersByOwnerId(ownerId);
                    break;
                case 8:
                    rentalController.viewAllCustomersByOwnerId(ownerId);
                    break;
                case 9:
                    ownerController.updateVehicleOwner(input, ownerId);
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
