package UI;

import admin.controller.AdminController;
import authentication.model.UserRole;
import customer.controller.CustomerController;
import util.InputUtil;
import vehicle.Controller.VehicleController;
import vehicleowner.Controller.VehicleOwnerController;

import java.util.Scanner;

public class AdminMenu implements UsersMenu {
    private final Scanner input;
    private final VehicleOwnerController ownerController;
    private final VehicleController vehicleController;
    private final CustomerController customerController;
    private final AdminController adminController;

    public AdminMenu(Scanner input, VehicleOwnerController ownerController, VehicleController vehicleController, CustomerController customerController, AdminController adminController) {
        this.input = input;
        this.ownerController = ownerController;
        this.vehicleController = vehicleController;
        this.customerController = customerController;
        this.adminController = adminController;
    }

    @Override
    public void show(UserRole role, String userId) {
        int choice;
        while (true) {
            System.out.println("\n========= ADMIN PANEL =========");
            System.out.println("--- Vehicle Owner Management ---");
            System.out.println("1.  View All Vehicle Owners");
            System.out.println("2.  Activate  Vehicle Owner");
            System.out.println("3.  Deactivate Vehicle Owner");
            System.out.println("--- Customer Management ---");
            System.out.println("4.  View All Customers");
            System.out.println("5.  Activate Customer");
            System.out.println("6.  Deactivate Customer");
            System.out.println("--- Vehicle Overview ---");
            System.out.println("7.  View All Vehicles");
            System.out.println("--- Rental Overview ---");
            System.out.println("8.  View All Active Rentals");
            System.out.println("9.  View Full Rental History");
            System.out.println("--- Admin account Management ---");
            System.out.println("10. Update My account");
            System.out.println("0.  Logout");

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
                case 4:
                    customerController.viewCustomers();
                    break;
                case 5:
                    customerController.activateCustomer(input);
                    break;
                case 6:
                    customerController.deactivateCustomer(input);
                    break;
                case 7:
                    vehicleController.viewVehicles();
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    adminController.updateAdmin(input, userId);
                    break;
                case 0:
                    System.out.println("Logged out from Admin.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
