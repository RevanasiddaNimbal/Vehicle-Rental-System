package Controller;

import Models.*;

import java.util.Scanner;

public class UpdateVehicles {
    private static Bike bike = new Bike();
    private static Auto auto = new Auto();
    private static Car car = new Car();

    protected static void UpdateBike(Bike bike, Scanner input) throws Exception {
        while (true) {
            System.out.println("\n===== Update Bike =====");
            System.out.println("1. Update Brand");
            System.out.println("2. Update Price");
            System.out.println("3. Update Category");
            System.out.println("4. Update Fuel Type");
            System.out.println("5. Update Engine Capacity");
            System.out.println("6. Update Status");
            System.out.println("0. Back");

            System.out.println("Enter Your Choice: ");
            int choice = input.nextInt();
            input.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter New Brand: ");
                    String newBrand = input.nextLine();
                    bike.setBrand(newBrand);
                    break;
                case 2:
                    System.out.print("Enter New Price: ");
                    double newPrice = input.nextDouble();
                    bike.setPricePerDay(newPrice);
                    break;
                case 3:
                    System.out.print("Choose New Category: \n");
                    VehicleCategory newCategory = EnumOperations.getCategory(input);
                    bike.setCategory(newCategory);
                    break;
                case 4:
                    System.out.print("Choose New Fuel Type: \n");
                    FuelType newFuel = EnumOperations.getFuel(input);
                    bike.setFuel(newFuel);
                    break;
                case 5:
                    System.out.println("Enter New Engine Capacity: ");
                    int newEngineCapacity = input.nextInt();
                    input.nextLine();
                    bike.setEnginCapacity(newEngineCapacity);
                    break;
                case 6:
                    System.out.println("Enter New Status: ");
                    VehicleStatus newStatus = EnumOperations.getStatus(input);
                    bike.setStatus(newStatus);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid Update Option.");
                    return;
            }
        }
    }

    protected static void UpdateCar(Car car, Scanner input) throws Exception {
        while (true) {
            System.out.println("\n===== Update Car =====");
            System.out.println("1. Update Brand");
            System.out.println("2. Update Price");
            System.out.println("3. Update Category");
            System.out.println("4. Update Fuel Type");
            System.out.println("5. Update seatingCapacity");
            System.out.println("6. Update Status");
            System.out.println("0. Back");

            System.out.println("Enter Your Choice: ");
            int choice = input.nextInt();
            input.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter New Brand: ");
                    String newBrand = input.nextLine();
                    car.setBrand(newBrand);
                    break;
                case 2:
                    System.out.print("Enter New Price: ");
                    double newPrice = input.nextDouble();
                    car.setPricePerDay(newPrice);
                    break;
                case 3:
                    System.out.print("Choose New Category: \n");
                    VehicleCategory newCategory = EnumOperations.getCategory(input);
                    car.setCategory(newCategory);
                    break;
                case 4:
                    System.out.print("Choose New Fuel Type: \n");
                    FuelType newFuel = EnumOperations.getFuel(input);
                    car.setFuel(newFuel);
                    break;
                case 5:
                    System.out.println("Enter New Seat Number: ");
                    int newSeating = input.nextInt();
                    input.nextLine();
                    car.setSeatingCapacity(newSeating);
                    break;
                case 6:
                    System.out.println("Enter New Status: ");
                    VehicleStatus newStatus = EnumOperations.getStatus(input);
                    car.setStatus(newStatus);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid Update Option.");
                    return;
            }
        }
    }

    protected static void UpdateAuto(Auto auto, Scanner input) throws Exception {
        while (true) {
            System.out.println("\n===== Update Car =====");
            System.out.println("1. Update Brand");
            System.out.println("2. Update Price");
            System.out.println("3. Update Category");
            System.out.println("4. Update seatingCapacity");
            System.out.println("5. Update Status");
            System.out.println("0. Back");

            System.out.print("Enter Your Choice: ");
            int choice = input.nextInt();
            input.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter New Brand: ");
                    String newBrand = input.nextLine();
                    auto.setBrand(newBrand);
                    break;
                case 2:
                    System.out.print("Enter New Price: ");
                    double newPrice = input.nextDouble();
                    auto.setPricePerDay(newPrice);
                    break;
                case 3:
                    System.out.print("Choose New Category: \n");
                    VehicleCategory newCategory = EnumOperations.getCategory(input);
                    auto.setCategory(newCategory);
                    break;
                case 4:
                    System.out.println("Enter New Seat Number: ");
                    int newSeating = input.nextInt();
                    input.nextLine();
                    auto.setSeatingCapacity(newSeating);
                    break;
                case 5:
                    System.out.println("Enter New Status: ");
                    VehicleStatus newStatus = EnumOperations.getStatus(input);
                    auto.setStatus(newStatus);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid Update Option.");
                    return;
            }
        }
    }
}
