package UI;


import authentication.model.UserRole;
import cancellation.controller.CancellationController;
import customer.controller.CustomerController;
import penalty.controller.PenaltyController;
import rental.controller.RentalController;
import util.InputUtil;
import vehicle.controller.VehicleController;

import java.util.Scanner;

public class CustomerMenu implements UsersMenu {
    private final Scanner input;
    private final CustomerController customerController;
    private final RentalController rentalController;
    private final VehicleController vehicleController;
    private final PenaltyController penaltyController;
    private final CancellationController cancellationController;

    public CustomerMenu(Scanner input, CustomerController customerController, RentalController rentalController, VehicleController vehicleController, PenaltyController penaltyController, CancellationController cancellationController) {
        this.input = input;
        this.customerController = customerController;
        this.rentalController = rentalController;
        this.vehicleController = vehicleController;
        this.penaltyController = penaltyController;
        this.cancellationController = cancellationController;
    }

    @Override
    public void show(UserRole role, String customerId) {
        int choice;
        while (true) {
            System.out.println("\n========= CUSTOMER PANEL ==============");
            System.out.println("1. View Available Vehicles");
            System.out.println("2. Rent a Vehicle");
            System.out.println("3. My Active Rental");
            System.out.println("4. Return a Vehicle");
            System.out.println("5. Cancel Booking of Vehicle");
            System.out.println("6. My Rental History");
            System.out.println("7. View My Active rental Vehicle Owners");
            System.out.println("8. View All My Rental Vehicle Owners");
            System.out.println("9. View My active rented vehicles");
            System.out.println("10. View My Rented vehicles");
            System.out.println("11. View My Penalties");
            System.out.println("12. view My Cancellations of rentals");
            System.out.println("13. Update My Account");
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
                    rentalController.viewActiveRentalsByCustomerId(customerId);
                    break;
                case 4:
                    rentalController.returnVehicle(input, customerId);
                    break;
                case 5:
                    rentalController.cancelRental(input, customerId);
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
                    rentalController.viewActiveVehiclesByCustomerId(customerId);
                    break;
                case 10:
                    rentalController.viewVehiclesByCustomerId(customerId);
                    break;
                case 11:
                    penaltyController.viewPenaltiesByCustomerId(customerId);
                    break;
                case 12:
                    cancellationController.viewCancellationsByCustomerId(customerId);
                    break;
                case 13:
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
