package vehicle.controller;

import UI.UserPrinter;
import exception.ServiceException;
import exception.ValidationException;
import exception.VehicleDeleteNotAllowedException;
import util.IdGeneratorUtil;
import util.IdPrefix;
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
    private final Map<Integer, VehicleCreator> creators;
    private final Map<Class<? extends Vehicle>, VehicleUpdater> updaters;
    private final UserPrinter<Vehicle> printer;

    public VehicleController(VehicleService service,
                             Map<Integer, VehicleCreator> creators,
                             Map<Class<? extends Vehicle>, VehicleUpdater> updaters,
                             UserPrinter<Vehicle> printer) {
        this.service = service;
        this.creators = creators;
        this.updaters = updaters;
        this.printer = printer;
    }

    public void addVehicle(Scanner input, String ownerId) {
        try {
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

            String id = IdGeneratorUtil.generate(IdPrefix.VEH);

            Vehicle vehicle = creator.createVehicle(id, input, ownerId);

            boolean saved = service.addVehicle(vehicle);
            if (saved) {
                System.out.println("Vehicle added successfully.");
                System.out.println("Vehicle ID: " + vehicle.getId());
            } else {
                System.out.println("Vehicle not added. Please try again.");
            }

        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        } catch (ServiceException se) {
            System.out.println("Service error: " + se.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    public void viewVehicles() {
        try {
            List<Vehicle> vehicles = service.getVehicles();
            if (vehicles == null || vehicles.isEmpty()) {
                System.out.println("No vehicles found.");
                return;
            }
            printer.print(vehicles);

        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        } catch (ServiceException se) {
            System.out.println("Service error: " + se.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    public void viewAvailableVehicles() {
        try {
            List<Vehicle> vehicles = service.getVehiclesByStatus(Status.AVAILABLE);
            if (vehicles == null || vehicles.isEmpty()) {
                System.out.println("No available vehicles found.");
                return;
            }
            printer.print(vehicles);

        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        } catch (ServiceException se) {
            System.out.println("Service error: " + se.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    public void viewVehicleByOwnerId(String ownerId) {
        try {
            List<Vehicle> vehicles = service.getVehiclesByOwnerId(ownerId);
            if (vehicles == null || vehicles.isEmpty()) {
                System.out.println("No vehicles found.");
                return;
            }
            printer.print(vehicles);

        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        } catch (ServiceException se) {
            System.out.println("Service error: " + se.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    public void updateVehicle(Scanner input, String ownerId) {
        try {
            String id = InputUtil.readString(input, "Enter Vehicle ID");

            Vehicle vehicle = service.getVehiclesById(id);

            if (vehicle == null) {
                System.out.println("Vehicle ID not exists.");
                return;
            }

            if (vehicle.getOwnerId() != null && !vehicle.getOwnerId().equals(ownerId)) {
                System.out.println("You are not allowed to update this vehicle.");
                return;
            }

            VehicleUpdater updater = updaters.get(vehicle.getClass());
            if (updater == null) {
                System.out.println("No updater found for this vehicle type.");
                return;
            }

            updater.updateVehicle(vehicle, input);

            boolean updated = service.updateVehicle(vehicle);
            if (updated) {
                System.out.println("Vehicle updated successfully.");
            } else {
                System.out.println("Vehicle update failed.");
            }

        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        } catch (ServiceException se) {
            System.out.println("Service error: " + se.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    public void deleteVehicle(Scanner input, String ownerId) {
        try {
            String id = InputUtil.readString(input, "Please Enter Vehicle ID");

            Vehicle vehicle = service.getVehiclesById(id);
            if (vehicle == null) {
                System.out.println("Vehicle not found.");
                return;
            }

            if (vehicle.getOwnerId() != null && !vehicle.getOwnerId().equals(ownerId)) {
                System.out.println("You are not allowed to delete this vehicle.");
                return;
            }

            boolean deleted = service.deleteVehicleById(id);
            if (deleted) {
                System.out.println("Vehicle deleted successfully.");
            } else {
                System.out.println("Vehicle not deleted.");
            }

        } catch (VehicleDeleteNotAllowedException ex) {
            System.out.println(ex.getMessage());
        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        } catch (ServiceException se) {
            System.out.println("Service error: " + se.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}