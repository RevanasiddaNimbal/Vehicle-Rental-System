package vehicle.controller;

import UI.UserPrinter;
import util.IdGeneratorUtil;
import util.InputUtil;
import vehicle.creator.VehicleCreator;
import vehicle.models.Status;
import vehicle.models.Vehicle;
import vehicle.service.VehicleService;
import vehicle.updater.VehicleUpdater;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VehicleController {
    private final VehicleService service;
    private final UserPrinter<Vehicle> printer;
    Map<Integer, VehicleCreator> creators;
    Map<Class<? extends Vehicle>, VehicleUpdater> updaters;


    public VehicleController(VehicleService service, Map<Integer, VehicleCreator> creators, Map<Class<? extends Vehicle>, VehicleUpdater> updaters, UserPrinter<Vehicle> printer) {
        this.service = service;
        this.creators = creators;
        this.updaters = updaters;
        this.printer = printer;
    }

    public void addVehicle(Scanner input, String ownerId) {
        System.out.println("\n-----Select Vehicle Type-----");
        System.out.println("1. Bike");
        System.out.println("2. Car");
        System.out.println("3. Auto");
        int choice = InputUtil.readPositiveInt(input, "Please Select Vehicle Type");
        VehicleCreator creator = creators.get(choice);
        if (creator == null) {
            System.out.println("Invalid choice!");
            return;
        }
        String id = IdGeneratorUtil.generateVehicleId();
        Vehicle vehicle = creator.createVehicle(id, input, ownerId);
        if (service.addVehicle(vehicle)) {
            System.out.println("Vehicle added successfully.");
            System.out.println("Vehicle ID: " + vehicle.getId());
        } else {
            System.out.println("Vehicle already exists.");
        }
    }

    public void viewAvailableVehicles() {
        List<Vehicle> vehicles = service.getVehiclesByStatus(Status.AVAILABLE);
        if (vehicles.isEmpty()) {
            System.out.println("No available vehicles found.");
            return;
        }
        printer.print(vehicles);
    }

    public void viewVehicles() {
        List<Vehicle> vehicles = service.getVehiclesByStatus(Status.AVAILABLE);
        if (vehicles.isEmpty()) {
            System.out.println("There are no vehicles available.");
            return;
        }
        printer.print(vehicles);
    }

    public void viewVehicleByOwnerId(String ownerId) {
        List<Vehicle> vehicles = service.getVehiclesByOwnerId(ownerId);
        if (vehicles.isEmpty()) {
            System.out.println("Vehicles not found.");
            return;
        }
        printer.print(vehicles);
    }

    public void updateVehicle(Scanner input) {
        String id = InputUtil.readString(input, "Enter Vehicle ID");
        Vehicle vehicle = service.getVehiclesById(id);
        if (vehicle == null) {
            System.out.println("Vehicle ID not exists.");
            return;
        }
        VehicleUpdater updater = updaters.get(vehicle.getClass());
        if (updater == null) {
            System.out.println("Failed to Update Vehicle.");
            return;
        }
        updater.updateVehicle(vehicle, input);
        boolean update = service.updateVehicle(vehicle);
        if (update)
            System.out.println("Vehicle updated successfully.");
        else System.out.println("Failed to Update Vehicle.");
    }

    public void deleteVehicle(Scanner input) {
        String id = InputUtil.readString(input, "Please Enter Vehicle ID");
        Vehicle vehicle = service.getVehiclesById(id);
        if (vehicle == null) {
            System.out.println("Vehicle ID: " + id + " not found.");
            return;
        }
        if (service.deleteVehicle(vehicle))
            System.out.println("Vehicle deleted successfully.");
        else
            System.out.println("Vehicle not deleted. Please try again.");
    }
}



