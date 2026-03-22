package config;

import admin.repository.AdminPostgresRepo;
import admin.repository.AdminRepo;
import cancellation.repository.CancellationPostgresRepo;
import cancellation.repository.CancellationRepo;
import customer.repository.CustomerRepo;
import customer.repository.CustomersPostgresRepo;
import penalty.repository.PenaltyPostgresRepo;
import penalty.repository.PenaltyRepo;
import rental.repository.RentalRepo;
import rental.repository.RentalsPostgresRepo;
import vehicle.repository.VehicleRepo;
import vehicle.repository.VehiclesPostgresRepo;
import vehicleowner.repository.VehicleOwnerRepo;
import vehicleowner.repository.VehicleOwnersPostgresRepo;
import wallet.repository.WalletCredentialPostgresRepo;
import wallet.repository.WalletCredentialRepo;
import wallet.repository.WalletPostgresRepo;
import wallet.repository.WalletRepo;

public class RepositoryConfig {

    private final DatabaseConfig databaseConfig;

    private AdminRepo adminRepo;
    private VehicleRepo vehicleRepo;
    private VehicleOwnerRepo vehicleOwnerRepo;
    private CustomerRepo customerRepo;
    private RentalRepo rentalRepo;
    private PenaltyRepo penaltyRepo;
    private CancellationRepo cancellationRepo;
    private WalletRepo walletRepo;
    private WalletCredentialRepo walletCredentialRepo;

    public RepositoryConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public AdminRepo getAdminRepo() {
        if (adminRepo == null) {
            adminRepo = new AdminPostgresRepo(databaseConfig.getPostgresConnection());
        }
        return adminRepo;
    }

    public VehicleRepo getVehicleRepo() {
        if (vehicleRepo == null) {
            vehicleRepo = new VehiclesPostgresRepo(databaseConfig.getPostgresConnection());
        }
        return vehicleRepo;
    }

    public VehicleOwnerRepo getVehicleOwnerRepo() {
        if (vehicleOwnerRepo == null) {
            vehicleOwnerRepo = new VehicleOwnersPostgresRepo(databaseConfig.getPostgresConnection());
        }
        return vehicleOwnerRepo;
    }

    public CustomerRepo getCustomerRepo() {
        if (customerRepo == null) {
            customerRepo = new CustomersPostgresRepo(databaseConfig.getPostgresConnection());
        }
        return customerRepo;
    }

    public RentalRepo getRentalRepo() {
        if (rentalRepo == null) {
            rentalRepo = new RentalsPostgresRepo(databaseConfig.getPostgresConnection());
        }
        return rentalRepo;
    }

    public PenaltyRepo getPenaltyRepo() {
        if (penaltyRepo == null) {
            penaltyRepo = new PenaltyPostgresRepo(databaseConfig.getPostgresConnection());
        }
        return penaltyRepo;
    }

    public CancellationRepo getCancellationRepo() {
        if (cancellationRepo == null) {
            cancellationRepo = new CancellationPostgresRepo(databaseConfig.getPostgresConnection());
        }
        return cancellationRepo;
    }

    public WalletRepo getWalletRepo() {
        if (walletRepo == null) {
            walletRepo = new WalletPostgresRepo(databaseConfig.getPostgresConnection());
        }
        return walletRepo;
    }

    public WalletCredentialRepo getWalletCredentialRepo() {
        if (walletCredentialRepo == null) {
            walletCredentialRepo = new WalletCredentialPostgresRepo(databaseConfig.getPostgresConnection());
        }
        return walletCredentialRepo;
    }
}