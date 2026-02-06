import Controller.ShowDocumentation;
import Controller.VehicleController;
import Service.VehicleService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            ShowDocumentation documentation = new ShowDocumentation();
            int Option;

            System.out.println("\n========================================================================");
            System.out.println("           WELCOME TO VEHICLE RENTAL MANAGEMENT SYSTEM                 ");
            System.out.println("========================================================================");
            while (true) {
                System.out.println("\n⭐ AVAILABLE OPTIONS ⭐");
                System.out.println("-------------------------");

                System.out.println("1. DOCUMENTATION SECTION.");
                System.out.println("2. VEHICLE MANAGEMENT SECTION.");
                System.out.println("3. CUSTOMER MANAGEMENT SECTION.");
                System.out.println("4. RENTAL MANAGEMENT SECTION.");
                System.out.println("0. EXIT");

                System.out.print("\nEnter your choice: ");
                Option = input.nextInt();
                input.nextLine();

                switch (Option) {
                    case 1:
                        documentation.display();
                        break;
                    case 2:
                        vehicleMenu(input);
                        break;
                    case 3:
                        customerMenu(input);
                        break;
                    case 4:
                        rentalMenu(input);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Exiting System...");
                        return;
                }

            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input. Please try again");
        }
    }

    private static void vehicleMenu(Scanner input) {
        try {
            VehicleService vehicleService = new VehicleService();
            VehicleController vehicleController = new VehicleController(vehicleService);
            while (true) {
                System.out.println("\n----- VEHICLE MANAGEMENT -----");
                System.out.println("1. Add New Vehicle");
                System.out.println("2. View All Vehicles");
                System.out.println("3. Update Vehicle Details");
                System.out.println("4. Delete Vehicle");
                System.out.println("0. Back");

                System.out.print("\nEnter your choice: ");
                int vehicleChoice = input.nextInt();
                input.nextLine();

                switch (vehicleChoice) {
                    case 1:
                        vehicleController.addVehicle(input);
                        break;
                    case 2:
                        vehicleController.viewVehicles();
                        break;
                    case 3:
                        vehicleController.UpdateVehicle(input);
                        break;
                    case 4:
                        vehicleController.deleteVehicle(input);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid Input. Please try again");
        }
    }

    private static void customerMenu(Scanner input) throws InputMismatchException {
        while (true) {

            System.out.println("\n----- CUSTOMER MANAGEMENT -----");
            System.out.println("1. Register New Customer");
            System.out.println("2. View All Customers");
            System.out.println("3. Search Customer");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("0. Back");

            System.out.print("\nEnter your choice: ");
            int customerChoice = input.nextInt();
            input.nextLine();

            switch (customerChoice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void rentalMenu(Scanner input) throws InputMismatchException {
        while (true) {

            System.out.println("\n----- RENTAL MANAGEMENT -----");
            System.out.println("1. Rent Vehicle");
            System.out.println("2. Return Vehicle");
            System.out.println("3. View Active Rentals");
            System.out.println("4. View Rental History");
            System.out.println("0. Back");

            System.out.print("\nEnter your choice: ");
            int rentalChoice = input.nextInt();
            input.nextLine();

            switch (rentalChoice) {
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
