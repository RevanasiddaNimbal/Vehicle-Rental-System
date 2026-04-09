package rental.command;

import customer.model.Customer;
import customer.service.CustomerService;
import invoice.model.Invoice;
import invoice.service.InvoiceService;
import payment.dto.PaymentDetails;
import payment.dto.WalletPaymentDetails;
import payment.factory.PaymentStrategyFactory;
import payment.model.PaymentMethod;
import rental.facade.RentalFacade;
import rental.model.Rental;
import rental.model.RentalStatus;
import rental.service.RentalService;
import util.IdGeneratorUtil;
import util.InputUtil;
import vehicle.models.Status;
import vehicle.models.Vehicle;
import vehicle.service.VehicleService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ScheduledFuture;

public class RentVehicleCommand implements RentalCommand {
    private final RentalFacade rentalFacade;
    private final RentalService rentalService;
    private final VehicleService vehicleService;
    private final CustomerService customerService;
    private final InvoiceService invoiceService;
    private final PaymentStrategyFactory paymentFactory;

    public RentVehicleCommand(RentalFacade rentalFacade, RentalService rentalService, VehicleService vehicleService,
                              CustomerService customerService, InvoiceService invoiceService, PaymentStrategyFactory paymentFactory) {
        this.rentalFacade = rentalFacade;
        this.rentalService = rentalService;
        this.vehicleService = vehicleService;
        this.customerService = customerService;
        this.invoiceService = invoiceService;
        this.paymentFactory = paymentFactory;
    }

    @Override
    public void execute(Scanner scanner, String customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        List<Rental> rentals = collectRentals(scanner, customerId);
        if (rentals.isEmpty()) return;

        invoiceService.printInvoice(new Invoice(customer, rentals));

        if (!InputUtil.readString(scanner, "Confirm Booking? (y/n)").trim().equalsIgnoreCase("y")) {
            System.out.println("Booking cancelled.");
            return;
        }

        PaymentMethod method = selectPaymentMethod(scanner);
        if (method == null) return;

        String pin = InputUtil.readValidPassword(scanner, "Enter your Wallet PIN to confirm payment:");
        PaymentDetails paymentDetails = new WalletPaymentDetails(pin);

        System.out.println("Processing Payment...");
        try {
            boolean success = rentalFacade.processBooking(customerId, rentals, method, paymentDetails, paymentFactory.getStrategy(method));
            if (success) {
                System.out.println("Booking successful! Your vehicle(s) are reserved. Enjoy your ride!");
            } else {
                System.out.println("Booking Failed due to payment error.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private List<Rental> collectRentals(Scanner input, String customerId) {
        List<Rental> rentals = new ArrayList<>();
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return null;
        }

        boolean choice = true;
        int count = 1;
        List<ScheduledFuture<?>> timeouts = new ArrayList<>();

        while (choice && count <= 3) {
            String vehicleId = InputUtil.readString(input, "Enter Vehicle ID");
            Vehicle vehicle = vehicleService.getVehiclesById(vehicleId);

            if (vehicle == null) {
                System.out.println("Invalid Vehicle ID. Please try again.");
                count++;
                continue;
            }
            if (vehicle.getStatus() != Status.AVAILABLE) {
                System.out.println("Vehicle is not available. Please choose a different vehicle.");
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
                System.out.println("Return time should be greater than start time. Please try again.");
                continue;
            }

            Rental rental = new Rental(rentalId, customerId, vehicleId, startDate, endDate, startTime, endTime, 0, vehicle.getPricePerDay(), 0, 0, 0, 0, RentalStatus.BOOKED);
            rental.setDays(rentalService.getDays(rental));
            rentalService.calculateTotalRent(rental);
            rentalService.calculateSecurityAmount(vehicle.getVehicle_type(), rental);
            rentals.add(rental);

            System.out.println("Vehicle added to booking list successfully!");

            String response = InputUtil.readString(input, "Add another vehicle? (Y/N)");
            if (!response.trim().equalsIgnoreCase("Y")) {
                choice = false;
            }
        }
        return rentals;
    }

    private PaymentMethod selectPaymentMethod(Scanner scanner) {
        while (true) {
            System.out.println("Select Payment Method:");
            System.out.println("1. Wallet");
            System.out.println("2. UPI (Coming Soon)");
            System.out.println("3. Credit/Debit Card (Coming Soon)");

            int choice = InputUtil.readPositiveInt(scanner, "Enter choice number:");
            switch (choice) {
                case 1:
                    return PaymentMethod.WALLET;
                case 2:
                    System.out.println("UPI payment is not available yet.Please select Wallet.");
                    break;
                case 3:
                    System.out.println("Card payment is not available yet.Please select Wallet.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
