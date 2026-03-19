package rental.controller;

import UI.UserPrinter;
import customer.model.Customer;
import customer.service.CustomerService;
import invoice.model.Invoice;
import invoice.service.InvoiceService;
import rental.model.Rental;
import rental.model.RentalStatus;
import rental.service.RentalService;
import util.IdGeneratorUtil;
import util.InputUtil;
import vehicle.models.Status;
import vehicle.models.Vehicle;
import vehicle.service.VehicleService;
import vehicleowner.models.VehicleOwner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RentalController {
    private final RentalService rentalService;
    private final VehicleService vehicleService;
    private final InvoiceService invoiceService;
    private final CustomerService customerService;
    private final UserPrinter<Rental> rentalPrinter;
    private final UserPrinter<Customer> customerPrinter;
    private final UserPrinter<VehicleOwner> ownerPrinter;

    public RentalController(RentalService rentalService, VehicleService vehicleService, InvoiceService invoiceService, CustomerService customerService, UserPrinter<Rental> rentalPrinter, UserPrinter<Customer> customerPrinter, UserPrinter<VehicleOwner> ownerPrinter) {
        this.rentalService = rentalService;
        this.vehicleService = vehicleService;
        this.invoiceService = invoiceService;
        this.customerService = customerService;
        this.rentalPrinter = rentalPrinter;
        this.customerPrinter = customerPrinter;
        this.ownerPrinter = ownerPrinter;

    }

    public void rentVehicle(Scanner input, String customerId) {
        List<Rental> rentals = new ArrayList<>();
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found");
            return;
        }
        boolean choice = true;

        while (choice) {

            String vehicleId = InputUtil.readString(input, "Enter Vehicle ID");

            Vehicle vehicle = vehicleService.getVehiclesById(vehicleId);

            if (vehicle == null) {
                System.out.println("Invalid Vehicle ID. Please try again.");
                continue;
            }
            if (vehicle.getStatus() == Status.RENTED) {
                System.out.println("Vehicle has been already renting.Please choose a different vehicle.");
                continue;
            }

            if (vehicle.getStatus() == Status.MAINTENANCE) {
                System.out.println("Vehicle is in maintenance.Please select different vehicle.");
            }

            int rentalId = IdGeneratorUtil.generateRentalId();
            LocalDate startDate = InputUtil.readValidDate(input, "Enter Start Date (DD/MM/YYYY)");
            LocalDate endDate = InputUtil.readValidDate(input, "Enter End Date (DD/MM/YYYY)");
            if (startDate.isAfter(endDate)) {
                System.out.println("End Date must be after Start Date.");
                continue;
            }
            LocalTime startTime = InputUtil.readValidTime(input, "Enter Pickup Time (HH:MM)");
            LocalTime endTime = InputUtil.readValidTime(input, "Enter return Time (HH:MM)");

            if (startDate.equals(endDate) && !endTime.isAfter(startTime)) {
                System.out.println("Return time should and must be greater than start time.Please try again");
                continue;
            }

            int days = rentalService.getDays(startDate, endDate);

            Rental rental = new Rental(rentalId, customerId, vehicleId, startDate, endDate, startTime, endTime, days, vehicle.getPricePerDay(), 0, 0, 0, RentalStatus.BOOKED);

            rentalService.calculateTotalRent(rental);
            rentals.add(rental);

            System.out.println("Vehicle added successfully!");
            String response = InputUtil.readString(input, "Add another vehicle? (Y/N)");

            if (!response.equalsIgnoreCase("Y")) {
                choice = false;
            }
        }
        if (rentals.isEmpty()) {
            System.out.println("No vehicles selected.");
            return;
        }

        Invoice invoice = new Invoice(customer, rentals);
        invoiceService.printInvoice(invoice);

        String confirm = InputUtil.readString(input, "Confirm Booking? (Y/N)");
        if (confirm.equalsIgnoreCase("Y")) {
            for (Rental rental : rentals) {
                vehicleService.updateStatusById(rental.getVehicleId(), Status.RENTED);
                rentalService.addRental(rental);
            }
            System.out.println("Booking successfully!");
        } else {
            System.out.println("Booking cancelled!");
        }

    }

    public void returnVehicle(Scanner input, String customerId) {
        int rentalId = InputUtil.readPositiveInt(input, "Enter Rental ID to return");

        Rental rental = rentalService.getRentalById(rentalId);
        if (rental == null) {
            System.out.println("Rental not found.");
            return;
        }
        if (!rental.getCustomerId().equals(customerId)) {
            System.out.println("This rental does not belong to you.");
            return;
        }

        if (rentalService.updateRentalStatus(rentalId, RentalStatus.COMPLETED)) {
            System.out.println("Vehicle returned successfully!");
        } else {
            System.out.println("Failed to return vehicle.");
        }
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