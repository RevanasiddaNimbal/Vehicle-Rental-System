package rental.controller;

import UI.UserPrinter;
import cancellation.service.CancellationService;
import customer.model.Customer;
import customer.service.CustomerService;
import exception.ResourceNotFoundException;
import invoice.model.Invoice;
import invoice.service.InvoiceService;
import payment.facade.PaymentFacade;
import payment.factory.PaymentStrategyFactory;
import payment.model.PaymentMethod;
import payment.stretegy.PaymentStrategy;
import penalty.model.Penalty;
import penalty.model.PenaltyType;
import penalty.service.PenaltyService;
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
    private final PenaltyService penaltyService;
    private final CancellationService cancellationService;
    private final PaymentFacade paymentFacade;
    private final PaymentStrategyFactory paymentFactory;
    private final UserPrinter<Rental> rentalPrinter;
    private final UserPrinter<Customer> customerPrinter;
    private final UserPrinter<VehicleOwner> ownerPrinter;
    private final UserPrinter<Vehicle> vehiclePrinter;
    private final UserPrinter<Penalty> penaltyPrinter;

    public RentalController(RentalService rentalService, VehicleService vehicleService, InvoiceService invoiceService, CustomerService customerService, PenaltyService penaltyService, CancellationService cancellationService, PaymentFacade paymentFacade, PaymentStrategyFactory paymentFactory, UserPrinter<Rental> rentalPrinter, UserPrinter<Customer> customerPrinter, UserPrinter<VehicleOwner> ownerPrinter, UserPrinter<Vehicle> vehiclePrinter, UserPrinter<Penalty> penaltyPrinter) {
        this.rentalService = rentalService;
        this.vehicleService = vehicleService;
        this.invoiceService = invoiceService;
        this.customerService = customerService;
        this.penaltyService = penaltyService;
        this.cancellationService = cancellationService;
        this.paymentFacade = paymentFacade;
        this.paymentFactory = paymentFactory;
        this.rentalPrinter = rentalPrinter;
        this.customerPrinter = customerPrinter;
        this.ownerPrinter = ownerPrinter;
        this.vehiclePrinter = vehiclePrinter;
        this.penaltyPrinter = penaltyPrinter;
    }

    public void rentVehicle(Scanner input, String customerId) {
        List<Rental> rentals = new ArrayList<>();
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        boolean choice = true;
        int count = 1;
        while (choice && count <= 3) {

            String vehicleId = InputUtil.readString(input, "Enter Vehicle ID");
            Vehicle vehicle = vehicleService.getVehiclesById(vehicleId);

            if (vehicle == null) {
                System.out.println("Invalid Vehicle ID. Please try again.");
                count++;
                continue;
            }

            if (vehicle.getStatus() != Status.AVAILABLE) {
                System.out.println("Vehicle is not available (Status: " + vehicle.getStatus() + "). Please choose a different vehicle.");
                continue;
            }

            int rentalId = IdGeneratorUtil.generateRentalId();
            LocalDate startDate = InputUtil.readValidDate(input, "Enter Start Date (DD/MM/YYYY)");
            LocalDate endDate = InputUtil.readValidDate(input, "Enter End Date (DD/MM/YYYY)");
            if (startDate.isAfter(endDate)) {
                System.out.println("End Date must be after Start Date.");
                continue;
            }

            LocalTime startTime = InputUtil.readValidTime(input, "Enter Pickup Time (in 24 Hours) (HH:MM)");
            LocalTime endTime = InputUtil.readValidTime(input, "Enter Return Time (in 24 Hours) (HH:MM)");
            if (startDate.equals(endDate) && !endTime.isAfter(startTime)) {
                System.out.println("Return time should be less than start time. Please try again.");
                continue;
            }

            Rental rental = new Rental(rentalId, customerId, vehicleId, startDate, endDate, startTime, endTime, 0, vehicle.getPricePerDay(), 0, 0, 0, 0, RentalStatus.BOOKED);
            rental.setDays(rentalService.getDays(rental));
            rentalService.calculateTotalRent(rental);
            rentalService.calculateSecurityAmount(vehicle.getVehicle_type(), rental);
            rentals.add(rental);

            System.out.println("Vehicle added to booking list successfully!");
            String response = InputUtil.readString(input, "Add another vehicle? (Y/N)");
            choice = response.equalsIgnoreCase("Y");
        }

        if (rentals.isEmpty()) {
            System.out.println("No vehicles selected.");
            return;
        }

        Invoice invoice = new Invoice(customer, rentals);
        invoiceService.printInvoice(invoice);

        String confirm = InputUtil.readString(input, "Confirm Booking? (Y/N)");
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Booking cancelled!");
            return;
        }

        System.out.println("Select Payment Method:");
        System.out.println("1. Wallet");
        System.out.println("2. Credit Card (Coming Soon)");
        int payChoice = InputUtil.readPositiveInt(input, "Enter choice");
        PaymentMethod method = (payChoice == 1) ? PaymentMethod.WALLET : PaymentMethod.WALLET;

        PaymentStrategy strategy = paymentFactory.getStrategy(method);

        System.out.println("Processing Payment...");
        boolean paymentSuccess = paymentFacade.processBookingPayments(input, customerId, rentals, strategy);

        if (!paymentSuccess) {
            System.out.println("Booking Failed due to payment error.");
            return;
        }

        rentalService.bookRentalsAsync(rentals)
                .thenAccept(result -> {
                    System.out.println("Your vehicles have been successfully booked.");
                })
                .exceptionally(ex -> {
                    System.out.println(" Booking Failed internally! Reason: " + ex.getCause().getMessage());
                    return null;
                });

        System.out.println("Your booking is processing...");
    }

    public void returnVehicle(Scanner input, String customerId) {
        List<Rental> rentalsToReturn = new ArrayList<>();
        boolean addMore = true;

        while (addMore) {
            int rentalId = InputUtil.readPositiveInt(input, "Enter Rental ID to return");
            try {
                Rental rental = rentalService.processReturnValidation(rentalId, customerId);
                rentalsToReturn.add(rental);
                System.out.println("Rental added for return.");
            } catch (ResourceNotFoundException | IllegalArgumentException | IllegalStateException e) {
                System.out.println("Validation Failed: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("System Error: Could not validate rental.");
            }

            String choice = InputUtil.readString(input, "Return another vehicle? (Y/N)");
            addMore = choice.equalsIgnoreCase("Y");
        }

        if (rentalsToReturn.isEmpty()) {
            System.out.println("No rentals to return.");
            return;
        }

        rentalPrinter.print(rentalsToReturn);
        List<Penalty> penalties = penaltyService.calculatePenalties(rentalsToReturn, PenaltyType.LATE_RETURN);

        if (!penalties.isEmpty()) {
            System.out.println("Penalties applied for return:");
            penaltyPrinter.print(penalties);
        }

        String message = !penalties.isEmpty() ? "Penalties apply. Confirm return? (Y/N)" : "No penalties. Confirm return? (Y/N)";
        String choice = InputUtil.readString(input, message);

        if (!choice.equalsIgnoreCase("Y")) {
            System.out.println("Return cancelled.");
            return;
        }

        System.out.println("Processing Escrow Payouts & Refunds...");
        paymentFacade.processReturnPayouts(customerId, rentalsToReturn, penalties);

        try {
            rentalService.completeRentals(rentalsToReturn);
            vehicleService.updateStatus(rentalsToReturn, Status.AVAILABLE);
            System.out.println("Vehicle(s) returned successfully! Payouts and refunds processed.");
        } catch (Exception e) {
            System.out.println("Failed to complete return status: " + e.getMessage());
        }
    }

    public void cancelRental(Scanner input, String customerId) {
        System.out.println("\n--- Cancel Rental ---");
        List<Rental> rentalsToCancel = new ArrayList<>();
        boolean addMore = true;

        while (addMore) {
            int rentalId = InputUtil.readPositiveInt(input, "Enter Rental ID to cancel");
            try {
                Rental rental = rentalService.processCancelValidation(rentalId, customerId);
                rentalsToCancel.add(rental);
                System.out.println("Rental added for cancellation.");
            } catch (ResourceNotFoundException | IllegalArgumentException | IllegalStateException e) {
                System.out.println("Validation Failed: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("System Error: Could not validate cancellation.");
            }

            String choice = InputUtil.readString(input, "Cancel another rental? (Y/N)");
            addMore = choice.equalsIgnoreCase("Y");
        }

        if (rentalsToCancel.isEmpty()) {
            System.out.println("No rentals to cancel.");
            return;
        }

        rentalPrinter.print(rentalsToCancel);
        String choice = InputUtil.readString(input, "Confirm cancellation? (Y/N)");

        if (!choice.equalsIgnoreCase("Y")) {
            System.out.println("Cancellation aborted.");
            return;
        }

        System.out.println("Processing Refunds...");
        for (Rental rental : rentalsToCancel) {
            double refundAmount = rental.getTotalPrice() + rental.getSecurityDeposit();
            paymentFacade.processCancellationRefund(customerId, rental, refundAmount);
        }

        try {
            for (Rental rental : rentalsToCancel) {
                cancellationService.cancelRentalByRentalId(rental.getId());
            }
            vehicleService.updateStatus(rentalsToCancel, Status.AVAILABLE);
            System.out.println("Cancellation processed successfully!");
        } catch (Exception e) {
            System.out.println("Failed to update status for cancellation: " + e.getMessage());
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