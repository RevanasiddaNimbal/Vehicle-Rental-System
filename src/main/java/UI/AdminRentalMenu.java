package UI;

import cancellation.controller.CancellationController;
import penalty.controller.PenaltyController;
import rental.controller.RentalController;
import util.InputUtil;

import java.util.Scanner;

public class AdminRentalMenu implements UsersMenu {
    private final RentalController rentalController;
    private final PenaltyController penaltyController;
    private final CancellationController cancellationController;
    private final Scanner input;

    public AdminRentalMenu(Scanner input, RentalController rentalController, PenaltyController penaltyController, CancellationController cancellationController) {
        this.input = input;
        this.rentalController = rentalController;
        this.penaltyController = penaltyController;
        this.cancellationController = cancellationController;
    }

    @Override
    public void show(String adminId) {
        int choice;
        while (true) {
            System.out.println("\n========= Rental Management Menu =========");
            System.out.println("1.  View All Active Rentals");
            System.out.println("2.  View Full Rental History");
            System.out.println("3.  View All Penalties");
            System.out.println("4.  View All Cancellations");
            System.out.println("0. back");
            choice = InputUtil.readPositiveInt(input, "Enter your choice");
            switch (choice) {
                case 1:
                    rentalController.viewActiveRentals();
                    break;
                case 2:
                    rentalController.viewAllRentals();
                    break;
                case 3:
                    penaltyController.viewPenalties();
                    break;
                case 4:
                    cancellationController.viewCancellations();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice");
            }

        }
    }
}
