package UI;

import rental.controller.RentalController;
import util.InputUtil;

import java.util.Scanner;

public class VehicleOwnerRentalsMenu implements UsersMenu {
    private final RentalController rentalController;
    private final Scanner input;

    public VehicleOwnerRentalsMenu(Scanner input, RentalController rentalController) {
        this.input = input;
        this.rentalController = rentalController;
    }

    @Override
    public void show(String ownerId) {
        int choice;
        while (true) {
            try {
                System.out.println("\n========= Vehicle Owners Rental Menu =============");
                System.out.println("1. View Active Rentals on My Vehicles");
                System.out.println("2. View My Active Customers");
                System.out.println("0. back");
                choice = InputUtil.readPositiveInt(input, "Enter your choice");
                switch (choice) {
                    case 1:
                        rentalController.viewActiveRentalsByOwnerId(ownerId);
                        break;
                    case 2:
                        rentalController.viewActiveCustomersByOwnerId(ownerId);
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
