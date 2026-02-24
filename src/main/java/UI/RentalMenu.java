package UI;

import util.InputUtil;

import java.util.Scanner;

public class RentalMenu implements Menu {
    private final Scanner input;

    public RentalMenu(Scanner input) {
        this.input = input;
    }

    @Override
    public void show() {
        int choice;
        while (true) {
            System.out.println("\n----- RENTAL MANAGEMENT -----");
            System.out.println("1. Rent Vehicle");
            System.out.println("2. Return Vehicle");
            System.out.println("3. View Active Rentals");
            System.out.println("4. View Rental History");
            System.out.println("0. Back");
            choice = InputUtil.readPositiveInt(input, "Enter your choice");

            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

}
