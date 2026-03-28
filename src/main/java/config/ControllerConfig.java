package config;

import admin.controller.AdminController;
import cancellation.controller.CancellationController;
import customer.controller.CustomerController;
import penalty.controller.PenaltyController;
import rental.controller.RentalController;
import transaction.controller.TransactionController;
import vehicle.controller.VehicleController;
import vehicleowner.controller.VehicleOwnerController;
import wallet.controller.WalletController;
import wallet.controller.WalletCredentialController;

import java.util.Scanner;

public class ControllerConfig {

    private final ServiceConfig serviceConfig;
    private final PrinterConfig printerConfig;
    private final StrategyConfig strategyConfig;
    private final Scanner input;

    private AdminController adminController;
    private VehicleController vehicleController;
    private VehicleOwnerController vehicleOwnerController;
    private CustomerController customerController;
    private PenaltyController penaltyController;
    private WalletController walletController;
    private WalletCredentialController walletCredentialController;
    private RentalController rentalController;
    private CancellationController cancellationController;
    private TransactionController transactionController;

    public ControllerConfig(ServiceConfig serviceConfig, PrinterConfig printerConfig, StrategyConfig strategyConfig, Scanner input) {
        this.serviceConfig = serviceConfig;
        this.printerConfig = printerConfig;
        this.strategyConfig = strategyConfig;
        this.input = input;
    }

    public AdminController getAdminController() {
        if (adminController == null) {
            adminController = new AdminController(serviceConfig.getAdminService());
        }
        return adminController;
    }

    public VehicleController getVehicleController() {
        if (vehicleController == null) {
            vehicleController = new VehicleController(
                    serviceConfig.getVehicleService(),
                    strategyConfig.getVehicleCreators(),
                    strategyConfig.getVehicleUpdaters(),
                    printerConfig.getVehiclePrinter()
            );
        }
        return vehicleController;
    }

    public VehicleOwnerController getVehicleOwnerController() {
        if (vehicleOwnerController == null) {
            vehicleOwnerController = new VehicleOwnerController(serviceConfig.getVehicleOwnerService(), printerConfig.getOwnerPrinter());
        }
        return vehicleOwnerController;
    }

    public CustomerController getCustomerController() {
        if (customerController == null) {
            customerController = new CustomerController(serviceConfig.getCustomerService(), printerConfig.getCustomerPrinter());
        }
        return customerController;
    }

    public PenaltyController getPenaltyController() {
        if (penaltyController == null) {
            penaltyController = new PenaltyController(serviceConfig.getPenaltyService(), printerConfig.getPenaltyPrinter());
        }
        return penaltyController;
    }

    public WalletController getWalletController() {
        if (walletController == null) {
            walletController = new WalletController(serviceConfig.getWalletService());
        }
        return walletController;
    }

    public WalletCredentialController getWalletCredentialController() {
        if (walletCredentialController == null) {
            walletCredentialController = new WalletCredentialController(input, serviceConfig.getWalletCredentialService(), serviceConfig.getWalletService());
        }
        return walletCredentialController;
    }

    public RentalController getRentalController() {
        if (rentalController == null) {
            rentalController = new RentalController(
                    serviceConfig.getRentalService(),
                    serviceConfig.getVehicleService(),
                    serviceConfig.getInvoiceService(),
                    serviceConfig.getCustomerService(),
                    serviceConfig.getPenaltyService(),
                    printerConfig.getRentalPrinter(),
                    printerConfig.getCustomerPrinter(),
                    printerConfig.getOwnerPrinter(),
                    printerConfig.getVehiclePrinter(),
                    printerConfig.getPenaltyPrinter(),
                    serviceConfig.getCancellationService(),
                    serviceConfig.getPaymentFacade(),
                    serviceConfig.getPaymentStrategyFactory(),
                    serviceConfig.getReservationTimeoutManager()
            );
        }
        return rentalController;
    }

    public CancellationController getCancellationController() {
        if (cancellationController == null) {
            cancellationController = new CancellationController(serviceConfig.getCancellationService(), printerConfig.getCancellationPrinter());
        }
        return cancellationController;
    }

    public TransactionController getTransactionController() {
        if (transactionController == null) {
            transactionController = new TransactionController(serviceConfig.getTransactionService(), serviceConfig.getWalletService(), printerConfig.getTransactionPrinter());
        }
        return transactionController;
    }
}