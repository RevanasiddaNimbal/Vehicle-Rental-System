package Controller;

import Models.FuelType;
import Models.VehicleCategory;
import Models.VehicleStatus;

import java.util.InputMismatchException;
import java.util.Scanner;

public class EnumOperations {
    protected static FuelType getFuel(Scanner input) {
        try {
            System.out.println("\n-----Select Fuel Type-----");
            System.out.println("1. PETROL");
            System.out.println("2. DIESEL");
            System.out.println("3. ELECTRIC");
            System.out.println("4. HYBRID");

            System.out.print("Enter your choice: ");
            int fuelChoice = input.nextInt();
            input.nextLine();

            FuelType fuel = null;
            switch (fuelChoice) {
                case 1:
                    fuel = FuelType.PETROL;
                    break;
                case 2:
                    fuel = FuelType.DIESEL;
                    break;
                case 3:
                    fuel = FuelType.ELECTRIC;
                    break;
                case 4:
                    fuel = FuelType.HYBRID;
                    break;
                default:
                    System.out.println("Invalid Fuel Type");
                    return null;
            }
            return fuel;
        } catch (Exception e) {
            System.out.println("Invalid Fuel Type");
            return null;
        }
    }

    protected static VehicleCategory getCategory(Scanner input) throws InputMismatchException {
        try {
            System.out.println("\n-----Select Category-----");
            System.out.println("----Car Category-----");
            System.out.println("1. SUV ");
            System.out.println("2. SEDAN ");
            System.out.println("3. HATCHBACK");
            System.out.println("----Bike Category-----");
            System.out.println("4. SPORTS");
            System.out.println("5. CRUISER");
            System.out.println("-----Auto Category-----");
            System.out.println("6. ELECTRIC_AUTO");
            System.out.println("7. CNG_AUTO");

            System.out.print("\nEnter your choice:");
            int choice = input.nextInt();
            input.nextLine();
            VehicleCategory category = null;
            switch (choice) {
                case 1:
                    category = VehicleCategory.SUV;
                    break;
                case 2:
                    category = VehicleCategory.SEDAN;
                    break;
                case 3:
                    category = VehicleCategory.HATCHBACK;
                    break;
                case 4:
                    category = VehicleCategory.SPORTS;
                    break;
                case 5:
                    category = VehicleCategory.CRUISER;
                    break;
                case 6:
                    category = VehicleCategory.ELECTRIC_AUTO;
                    break;
                case 7:
                    category = VehicleCategory.CNG_AUTO;
                    break;
                default:
                    System.out.println("Invalid Category");
                    return null;
            }
            return category;
        } catch (Exception e) {
            System.out.println("Invalid Category");
            return null;
        }
    }

    protected static VehicleStatus getStatus(Scanner input) throws InputMismatchException {
        System.out.println("-----Select Status-----");
        System.out.println("1. AVAILABLE");
        System.out.println("2. RENTED");
        System.out.println("3. MAINTENANCE");

        System.out.println("Enter your Choice: ");
        int choice = input.nextInt();
        input.nextLine();
        VehicleStatus status = null;
        switch (choice) {
            case 1:
                status = VehicleStatus.AVAILABLE;
                break;
            case 2:
                status = VehicleStatus.RENTED;
                break;
            case 3:
                status = VehicleStatus.MAINTENANCE;
                break;
            default:
                System.out.println("Invalid Status");
                return null;
        }
        return status;
    }

}
