package UI;


import authentication.model.UserRole;
import customer.controller.CustomerController;
import rental.controller.RentalController;
import util.InputUtil;
import vehicle.controller.VehicleController;

import java.util.Scanner;

public class CustomerMenu implements UsersMenu {
    private final Scanner input;
    private final CustomerController customerController;
    private final RentalController rentalController;
    private final VehicleController vehicleController;

    public CustomerMenu(Scanner input, CustomerController customerController, RentalController rentalController, VehicleController vehicleController) {
        this.input = input;
        this.customerController = customerController;
        this.rentalController = rentalController;
        this.vehicleController = vehicleController;
    }

    @Override
    public void show(UserRole role, String customerId) {
        int choice;
        while (true) {
            System.out.println("\n========= CUSTOMER PANEL ==============");
            System.out.println("1. View Available Vehicles");
            System.out.println("2. Rent a Vehicle");
            System.out.println("3. Return a Vehicle");
            System.out.println("4. Cancel Booking of Vehicle");
            System.out.println("5. My Active Rental");
            System.out.println("6. My Rental History");
            System.out.println("7. View My Active rental Vehicle Owners");
            System.out.println("8. View All My Rental Vehicle Owners");
            System.out.println("9. Update My Account");
            System.out.println("0. Logout");

            choice = InputUtil.readPositiveInt(input, "Enter your choice");

            switch (choice) {
                case 1:
                    vehicleController.viewAvailableVehicles();
                    break;
                case 2:
                    rentalController.rentVehicle(input, customerId);
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    rentalController.viewActiveRentalsByCustomerId(customerId);
                    break;
                case 6:
                    rentalController.viewRentalsByCustomerId(customerId);
                    break;
                case 7:
                    rentalController.viewActiveOwnersByCustomerId(customerId);
                    break;
                case 8:
                    rentalController.viewAllOwnersByCustomerId(customerId);
                    break;
                case 9:
                    customerController.updateCustomer(input, customerId);
                    break;
                case 0:
                    System.out.println("Logged out from Customer panel.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
