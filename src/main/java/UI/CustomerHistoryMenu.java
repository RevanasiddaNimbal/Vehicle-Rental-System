package UI;

import cancellation.controller.CancellationController;
import penalty.controller.PenaltyController;
import rental.controller.RentalController;
import util.InputUtil;

import java.util.Scanner;

public class CustomerHistoryMenu implements UsersMenu {
    private final RentalController rentalController;
    private final PenaltyController penaltyController;
    private final CancellationController cancellationController;
    private final Scanner input;

    public CustomerHistoryMenu(Scanner input, RentalController rentalController, PenaltyController penaltyController, CancellationController cancellationController) {
        this.input = input;
        this.rentalController = rentalController;
        this.penaltyController = penaltyController;
        this.cancellationController = cancellationController;
    }

    @Override
    public void show(String customerId) {
        int choice;
        while (true) {
            System.out.println("\n========= Customer History Management Menu ==============");
            System.out.println("1. My Rental History");
            System.out.println("2. View My Rented vehicles");
            System.out.println("3. View  My Rental Vehicle Owners");
            System.out.println("4. View My Penalties");
            System.out.println("5. view My Cancellations of rentals");
            System.out.println("0. back");
            choice = InputUtil.readPositiveInt(input, "Enter your choice");
            switch (choice) {
                case 1:
                    rentalController.viewRentalsByCustomerId(customerId);
                    break;
                case 2:
                    rentalController.viewVehiclesByCustomerId(customerId);
                    break;
                case 3:
                    rentalController.viewAllOwnersByCustomerId(customerId);
                    break;
                case 4:
                    penaltyController.viewPenaltiesByCustomerId(customerId);
                    break;
                case 5:
                    cancellationController.viewCancellationsByCustomerId(customerId);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.Please try again");
            }
        }
    }
}
