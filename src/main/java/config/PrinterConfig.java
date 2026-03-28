package config;

import UI.*;
import cancellation.model.CancellationRecord;
import customer.model.Customer;
import penalty.model.Penalty;
import rental.model.Rental;
import transaction.model.Transaction;
import vehicle.models.Vehicle;
import vehicleowner.models.VehicleOwner;

public class PrinterConfig {

    private UserPrinter<Customer> customerPrinter;
    private UserPrinter<Vehicle> vehiclePrinter;
    private UserPrinter<VehicleOwner> ownerPrinter;
    private UserPrinter<Rental> rentalPrinter;
    private UserPrinter<Penalty> penaltyPrinter;
    private UserPrinter<CancellationRecord> cancellationPrinter;
    private UserPrinter<Transaction> transactionPrinter;

    public UserPrinter<Customer> getCustomerPrinter() {
        if (customerPrinter == null) {
            customerPrinter = new CustomerPrinter();
        }
        return customerPrinter;
    }

    public UserPrinter<Vehicle> getVehiclePrinter() {
        if (vehiclePrinter == null) {
            vehiclePrinter = new VehiclePrinter();
        }
        return vehiclePrinter;
    }

    public UserPrinter<VehicleOwner> getOwnerPrinter() {
        if (ownerPrinter == null) {
            ownerPrinter = new OwnersPrinter();
        }
        return ownerPrinter;
    }

    public UserPrinter<Rental> getRentalPrinter() {
        if (rentalPrinter == null) {
            rentalPrinter = new RentalPrinter();
        }
        return rentalPrinter;
    }

    public UserPrinter<Penalty> getPenaltyPrinter() {
        if (penaltyPrinter == null) {
            penaltyPrinter = new PenaltyPrinter();
        }
        return penaltyPrinter;
    }

    public UserPrinter<CancellationRecord> getCancellationPrinter() {
        if (cancellationPrinter == null) {
            cancellationPrinter = new CancellationPrinter();
        }
        return cancellationPrinter;
    }

    public UserPrinter<Transaction> getTransactionPrinter() {
        if (transactionPrinter == null) {
            transactionPrinter = new TransactionPrinter();
        }
        return transactionPrinter;
    }
}