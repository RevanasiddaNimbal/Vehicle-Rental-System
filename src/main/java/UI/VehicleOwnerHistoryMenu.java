package UI;

import cancellation.controller.CancellationController;
import rental.controller.RentalController;
import util.InputUtil;

import java.util.Scanner;

public class VehicleOwnerHistoryMenu implements UsersMenu {
    private final RentalController rentalController;
    private final CancellationController cancellationController;
    private final Scanner input;

    public VehicleOwnerHistoryMenu(Scanner input, RentalController rentalController, CancellationController cancellationController) {
        this.input = input;
        this.rentalController = rentalController;
        this.cancellationController = cancellationController;
    }

    @Override
    public void show(String ownerId) {
        int choice;
        while (true) {
            try {
                System.out.println("\n========= Vehicle Owner History Management Menu ==============");
                System.out.println("1. View Rental History of My Vehicles");
                System.out.println("2. View My All Customers");
                System.out.println("3. View Cancellations on My Vehicles");
                System.out.println("0. back");
                choice = InputUtil.readPositiveInt(input, "Enter your choice");
                switch (choice) {
                    case 1:
                        rentalController.viewRentalsByOwnerId(ownerId);
                        break;
                    case 2:
                        rentalController.viewAllCustomersByOwnerId(ownerId);
                        break;
                    case 3:
                        cancellationController.viewCancellationsByOwnerId(ownerId);
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
