package rental.controller;

import UI.UserPrinter;
import customer.model.Customer;
import rental.command.RentalCommand;
import rental.model.Rental;
import rental.model.RentalCommands;
import rental.service.RentalService;
import vehicle.models.Vehicle;
import vehicleowner.models.VehicleOwner;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RentalController {
    private final RentalService rentalService;
    private final UserPrinter<Rental> rentalPrinter;
    private final Map<RentalCommands, RentalCommand> commandMap;
    private final UserPrinter<Customer> customerPrinter;
    private final UserPrinter<VehicleOwner> ownerPrinter;
    private final UserPrinter<Vehicle> vehiclePrinter;

    public RentalController(RentalService rentalService, Map<RentalCommands, RentalCommand> commands,
                            UserPrinter<Rental> rentalPrinter, UserPrinter<Customer> customerPrinter,
                            UserPrinter<VehicleOwner> ownerPrinter, UserPrinter<Vehicle> vehiclePrinter) {
        this.rentalService = rentalService;
        this.commandMap = commands;
        this.rentalPrinter = rentalPrinter;
        this.customerPrinter = customerPrinter;
        this.ownerPrinter = ownerPrinter;
        this.vehiclePrinter = vehiclePrinter;
    }

    public void rentVehicle(Scanner input, String customerId) {
        RentalCommand rentCommand = commandMap.get(RentalCommands.RENT_COMMAND);
        if (rentCommand == null) {
            System.out.println("Rent command not found.");
            return;
        }
        rentCommand.execute(input, customerId);
    }

    public void returnVehicle(Scanner input, String customerId) {
        RentalCommand returnCommand = commandMap.get(RentalCommands.RETURN_COMMAND);
        if (returnCommand == null) {
            System.out.println("Return command not found.");
            return;
        }
        returnCommand.execute(input, customerId);
    }

    public void cancelRental(Scanner input, String customerId) {
        RentalCommand cancelCommand = commandMap.get(RentalCommands.CANCEL_COMMAND);
        if (cancelCommand == null) {
            System.out.println("Cancel command not found.");
            return;
        }
        cancelCommand.execute(input, customerId);
    }

    public void viewActiveRentalsByCustomerId(String customerId) {
        List<Rental> rentals = rentalService.getActiveRentalsByCustomerId(customerId);
        if (rentals.isEmpty()) {
            System.out.println("No active rentals found.");
            return;
        }
        rentalPrinter.print(rentals);
    }

    public void viewRentalsByCustomerId(String customerId) {
        List<Rental> rentals = rentalService.getRentalsByCustomerId(customerId);
        if (rentals.isEmpty()) {
            System.out.println("No rentals found.");
            return;
        }
        rentalPrinter.print(rentals);
    }

    public void viewActiveRentalsByOwnerId(String ownerId) {
        List<Rental> rentals = rentalService.getActiveRentalsByOwnerId(ownerId);
        if (rentals.isEmpty()) {
            System.out.println("No active rentals found.");
            return;
        }
        rentalPrinter.print(rentals);
    }

    public void viewRentalsByOwnerId(String ownerId) {
        List<Rental> rentals = rentalService.getRentalsByOwnerId(ownerId);
        if (rentals.isEmpty()) {
            System.out.println("No rentals found.");
            return;
        }
        rentalPrinter.print(rentals);
    }

    public void viewActiveCustomersByOwnerId(String ownerId) {
        List<Customer> customers = rentalService.getActiveCustomersByOwnerId(ownerId);
        if (customers.isEmpty()) {
            System.out.println("No active customers found.");
            return;
        }
        customerPrinter.print(customers);
    }

    public void viewAllCustomersByOwnerId(String ownerId) {
        List<Customer> customers = rentalService.getAllCustomersByOwnerId(ownerId);
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }
        customerPrinter.print(customers);
    }

    public void viewActiveOwnersByCustomerId(String customerId) {
        List<VehicleOwner> vehicleOwners = rentalService.getActiveOwnersByCustomerId(customerId);
        if (vehicleOwners.isEmpty()) {
            System.out.println("No vehicle owners found.");
            return;
        }
        ownerPrinter.print(vehicleOwners);
    }

    public void viewAllOwnersByCustomerId(String customerId) {
        List<VehicleOwner> vehicleOwners = rentalService.getAllOwnersByCustomerId(customerId);
        if (vehicleOwners.isEmpty()) {
            System.out.println("No vehicle owners found.");
            return;
        }
        ownerPrinter.print(vehicleOwners);
    }

    public void viewActiveVehiclesByCustomerId(String customerId) {
        List<Vehicle> vehicles = rentalService.getActiveVehiclesByCustomerId(customerId);
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }
        vehiclePrinter.print(vehicles);
    }

    public void viewVehiclesByCustomerId(String customerId) {
        List<Vehicle> vehicles = rentalService.getVehiclesByCustomerId(customerId);
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }
        vehiclePrinter.print(vehicles);
    }

    public void viewAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        if (rentals.isEmpty()) {
            System.out.println("No rentals found.");
            return;
        }
        rentalPrinter.print(rentals);
    }

    public void viewActiveRentals() {
        List<Rental> rentals = rentalService.getActiveRentals();
        if (rentals.isEmpty()) {
            System.out.println("No active rentals found.");
            return;
        }
        rentalPrinter.print(rentals);
    }
}