package Controller;

import Models.*;
import Service.VehicleService;

import java.util.Scanner;

public class VehicleController {
    private final VehicleService service;
    private static int idCounter = 1;

    public VehicleController(VehicleService service) {
        this.service = service;

    }

    // Auto id generation.
    private String idGenerator() {
        String format = String.format("VEH-%03d", idCounter++);
        return format;
    }

    // Adding new vehicles.
    public void addVehicle(Scanner input) throws Exception {

        System.out.println("\n-----Select Vehicle Type-----");
        System.out.println("1. Bike");
        System.out.println("2. Car");
        System.out.println("3. Auto");

        int chioce = input.nextInt();
        input.nextLine();
        String id = idGenerator();

        System.out.print("Enter Brand: ");
        String brand = input.nextLine();

        VehicleCategory category = EnumOperations.getCategory(input);

        System.out.print("Enter Price Per Day: ");
        double price = input.nextDouble();
        input.nextLine();

        Vehicle vehicle = null;
        switch (chioce) {
            case 1:

                System.out.println();
                System.out.print("Enter Engin Capacity(cc): ");
                int capacity = input.nextInt();
                input.nextLine();
                FuelType fuel = EnumOperations.getFuel(input);
                vehicle = new Bike(id, brand, category, price, fuel, capacity);
                boolean check_bike = service.addVehicle(vehicle);
                if (check_bike) {
                    System.out.println("Vehicle added successfully.");
                    System.out.println("Vehicle ID: " + vehicle.getId());
                } else {
                    System.out.println("Vehicle already exists.");
                }
                break;
            case 2:
                System.out.print("Enter Setting Capacity: ");
                int settingCapacity = input.nextInt();
                input.nextLine();
                FuelType fuelType = EnumOperations.getFuel(input);
                vehicle = new Car(id, brand, category, price, settingCapacity, fuelType);
                boolean check_car = service.addVehicle(vehicle);
                if (check_car) {
                    System.out.println("Vehicle added successfully.");
                    System.out.println("Vehicle ID: " + vehicle.getId());
                } else {
                    System.out.println("Vehicle already exists.");
                }
                break;
            case 3:
                System.out.print("Enter Number of Seats: ");
                int seats = input.nextInt();
                input.nextLine();
                vehicle = new Auto(id, brand, category, price, seats);
                boolean check_auto = service.addVehicle(vehicle);
                if (check_auto) {
                    System.out.println("Vehicle added successfully.");
                    System.out.println("Vehicle ID: " + vehicle.getId());
                } else {
                    System.out.println("Vehicle already exists.");
                }
                break;
            default:
                System.out.println("Invalid Choice");
                break;
        }

    }

    // Displaying Vehicle Logic.
    public void viewVehicles() {
        if (service.getVehicles().isEmpty()) {
            System.out.println("No vehicles available.");
            return;
        }
        for (Vehicle v : service.getVehicles()) {
            System.out.println(v);
        }
    }

    public void UpdateVehicle(Scanner input) throws Exception {
        System.out.print("Enter Vehicle ID: ");
        String id = input.nextLine();

        Vehicle vehicle = service.findById(id);

        if (vehicle == null) {
            System.out.println("Vehicle not found!");
            return;
        }
        if (vehicle instanceof Bike) {
            UpdateVehicles.UpdateBike((Bike) vehicle, input);
        } else if (vehicle instanceof Car) {
            UpdateVehicles.UpdateCar((Car) vehicle, input);
        } else if (vehicle instanceof Auto) {
            UpdateVehicles.UpdateAuto((Auto) vehicle, input);
        } else {
            System.out.println("Invalid Vehicle Type.");
        }
    }

    // Deleting Vehicle Logic.
    public void deleteVehicle(Scanner input) throws Exception {
        System.out.print("Enter Vehicle Id to delete: ");
        String id = input.nextLine();
        Vehicle vehicle = service.findById(id);
        if (vehicle == null) {
            System.out.println("Vehicle Not Found");
            return;
        }
        if (service.deleteVehicle(vehicle))
            System.out.println("Vehicle Deleted successfully.");
        else
            System.out.println("Vehicle Not Deleted.Please Try Again");
    }

}



