package UI;

import rental.controller.RentalController;
import util.InputUtil;
import vehicle.controller.VehicleController;

import java.util.Scanner;

public class CustomerRentalsMenu implements UsersMenu {
    private final RentalController rentalController;
    private final VehicleController vehicleController;
    private final Scanner input;

    public CustomerRentalsMenu(Scanner input, VehicleController vehicleController, RentalController rentalController) {
        this.input = input;
        this.vehicleController = vehicleController;
        this.rentalController = rentalController;
    }

    @Override
    public void show(String customerId) {
        int choice;
        while (true) {
            System.out.println("\n========= Customer Rental Management Menu ==============");
            System.out.println("1. View Available Vehicles");
            System.out.println("2. Rent a Vehicle");
            System.out.println("3. My Active Rental");
            System.out.println("4. Return a Vehicle");
            System.out.println("5. View My active rented vehicles");
            System.out.println("6. View My Active rental Vehicle Owners");
            System.out.println("7. Cancel Booking of Vehicle");
            System.out.println("0. back");
            choice = InputUtil.readPositiveInt(input, "Enter Your choice");
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
                    rentalController.viewActiveVehiclesByCustomerId(customerId);
                    break;
                case 6:
                    rentalController.viewActiveOwnersByCustomerId(customerId);
                    break;
                case 7:
                    rentalController.cancelRental(input, customerId);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.Please try again");
            }
        }

    }
}
