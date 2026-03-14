package vehicle.Controller;

import util.IdGenerator;
import util.InputUtil;
import vehicle.Creator.VehicleCreator;
import vehicle.Models.Vehicle;
import vehicle.Service.VehicleService;
import vehicle.Updater.VehicleUpdater;

import java.util.Map;
import java.util.Scanner;

import static util.OutputUtil.valueOrDash;

public class VehicleController {
    private final VehicleService service;
    Map<Integer, VehicleCreator> creators;
    Map<Class<? extends Vehicle>, VehicleUpdater> updaters;

    public VehicleController(VehicleService service, Map<Integer, VehicleCreator> creators, Map<Class<? extends Vehicle>, VehicleUpdater> updaters) {
        this.service = service;
        this.creators = creators;
        this.updaters = updaters;
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
        String id = IdGenerator.generateVehicleId();
        Vehicle vehicle = creator.createVehicle(id, input, ownerId);
        if (service.addVehicle(vehicle)) {
            System.out.println("Vehicle added successfully.");
            System.out.println("Vehicle ID: " + vehicle.getId());
        } else {
            System.out.println("Vehicle already exists.");
        }
    }

    public void viewVehicles() {
        if (service.getVehicles().isEmpty()) {
            System.out.println("There are no vehicles available.");
            return;
        }
        System.out.println("-----------------------------------------------------------------------------------------------");
        System.out.printf(
                "%-10s %-15s %-12s %-10s %-12s %-10s %-8s %-12s%n",
                "ID", "Brand", "Category", "Price", "Status",
                "Engine CC", "Seats", "Fuel"
        );
        System.out.println("-----------------------------------------------------------------------------------------------");

        for (Vehicle v : service.getVehicles()) {
            System.out.printf(
                    "%-10s %-15s %-12s ₹%-9.2f %-12s %-11s %-8s %-12s%n",
                    v.getId(),
                    v.getBrand(),
                    v.getCategory(),
                    v.getPricePerDay(),
                    v.getStatus(),
                    valueOrDash(v.getEnginCapacity()),
                    valueOrDash(v.getSeatingCapacity()),
                    valueOrDash(v.getFuelType())
            );
        }

        System.out.println("-----------------------------------------------------------------------------------------------");
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



