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

    private volatile UserPrinter<Customer> customerPrinter;
    private volatile UserPrinter<Vehicle> vehiclePrinter;
    private volatile UserPrinter<VehicleOwner> ownerPrinter;
    private volatile UserPrinter<Rental> rentalPrinter;
    private volatile UserPrinter<Penalty> penaltyPrinter;
    private volatile UserPrinter<CancellationRecord> cancellationPrinter;
    private volatile UserPrinter<Transaction> transactionPrinter;

    public UserPrinter<Customer> getCustomerPrinter() {
        if (customerPrinter == null) {
            synchronized (this) {
                if (customerPrinter == null) {
                    customerPrinter = new CustomerPrinter();
                }
            }
        }
        return customerPrinter;
    }

    public UserPrinter<Vehicle> getVehiclePrinter() {
        if (vehiclePrinter == null) {
            synchronized (this) {
                if (vehiclePrinter == null) {
                    vehiclePrinter = new VehiclePrinter();
                }
            }
        }
        return vehiclePrinter;
    }

    public UserPrinter<VehicleOwner> getOwnerPrinter() {
        if (ownerPrinter == null) {
            synchronized (this) {
                if (ownerPrinter == null) {
                    ownerPrinter = new OwnersPrinter();
                }
            }
        }
        return ownerPrinter;
    }

    public UserPrinter<Rental> getRentalPrinter() {
        if (rentalPrinter == null) {
            synchronized (this) {
                if (rentalPrinter == null) {
                    rentalPrinter = new RentalPrinter();
                }
            }
        }
        return rentalPrinter;
    }

    public UserPrinter<Penalty> getPenaltyPrinter() {
        if (penaltyPrinter == null) {
            synchronized (this) {
                if (penaltyPrinter == null) {
                    penaltyPrinter = new PenaltyPrinter();
                }
            }
        }
        return penaltyPrinter;
    }

    public UserPrinter<CancellationRecord> getCancellationPrinter() {
        if (cancellationPrinter == null) {
            synchronized (this) {
                if (cancellationPrinter == null) {
                    cancellationPrinter = new CancellationPrinter();
                }
            }
        }
        return cancellationPrinter;
    }

    public UserPrinter<Transaction> getTransactionPrinter() {
        if (transactionPrinter == null) {
            synchronized (this) {
                if (transactionPrinter == null) {
                    transactionPrinter = new TransactionPrinter();
                }
            }
        }
        return transactionPrinter;
    }
}