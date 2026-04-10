package config;

import admin.repository.AdminPostgresRepo;
import admin.repository.AdminRepo;
import cancellation.repository.CancellationPostgresRepo;
import cancellation.repository.CancellationRepo;
import customer.repository.CustomerRepo;
import customer.repository.CustomersPostgresRepo;
import otp.repository.OtpPostgresRepo;
import otp.repository.OtpRepo;
import penalty.repository.PenaltyPostgresRepo;
import penalty.repository.PenaltyRepo;
import rental.repository.RentalRepo;
import rental.repository.RentalsPostgresRepo;
import transaction.repository.TransactionPostgresRepo;
import transaction.repository.TransactionRepo;
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

    private volatile AdminRepo adminRepo;
    private volatile VehicleRepo vehicleRepo;
    private volatile VehicleOwnerRepo vehicleOwnerRepo;
    private volatile CustomerRepo customerRepo;
    private volatile RentalRepo rentalRepo;
    private volatile PenaltyRepo penaltyRepo;
    private volatile CancellationRepo cancellationRepo;
    private volatile WalletRepo walletRepo;
    private volatile WalletCredentialRepo walletCredentialRepo;
    private volatile TransactionRepo transactionRepo;
    private volatile OtpRepo otpRepo;

    public RepositoryConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public AdminRepo getAdminRepo() {
        if (adminRepo == null) {
            synchronized (this) {
                if (adminRepo == null) {
                    adminRepo = new AdminPostgresRepo(databaseConfig.getPostgresConnection());
                }
            }
        }
        return adminRepo;
    }

    public VehicleRepo getVehicleRepo() {
        if (vehicleRepo == null) {
            synchronized (this) {
                if (vehicleRepo == null) {
                    vehicleRepo = new VehiclesPostgresRepo(databaseConfig.getPostgresConnection());
                }
            }
        }
        return vehicleRepo;
    }

    public VehicleOwnerRepo getVehicleOwnerRepo() {
        if (vehicleOwnerRepo == null) {
            synchronized (this) {
                if (vehicleOwnerRepo == null) {
                    vehicleOwnerRepo = new VehicleOwnersPostgresRepo(databaseConfig.getPostgresConnection());
                }
            }
        }
        return vehicleOwnerRepo;
    }

    public CustomerRepo getCustomerRepo() {
        if (customerRepo == null) {
            synchronized (this) {
                if (customerRepo == null) {
                    customerRepo = new CustomersPostgresRepo(databaseConfig.getPostgresConnection());
                }
            }
        }
        return customerRepo;
    }

    public RentalRepo getRentalRepo() {
        if (rentalRepo == null) {
            synchronized (this) {
                if (rentalRepo == null) {
                    rentalRepo = new RentalsPostgresRepo(databaseConfig.getPostgresConnection());
                }
            }
        }
        return rentalRepo;
    }

    public PenaltyRepo getPenaltyRepo() {
        if (penaltyRepo == null) {
            synchronized (this) {
                if (penaltyRepo == null) {
                    penaltyRepo = new PenaltyPostgresRepo(databaseConfig.getPostgresConnection());
                }
            }
        }
        return penaltyRepo;
    }

    public CancellationRepo getCancellationRepo() {
        if (cancellationRepo == null) {
            synchronized (this) {
                if (cancellationRepo == null) {
                    cancellationRepo = new CancellationPostgresRepo(databaseConfig.getPostgresConnection());
                }
            }
        }
        return cancellationRepo;
    }

    public WalletRepo getWalletRepo() {
        if (walletRepo == null) {
            synchronized (this) {
                if (walletRepo == null) {
                    walletRepo = new WalletPostgresRepo(databaseConfig.getPostgresConnection());
                }
            }
        }
        return walletRepo;
    }

    public WalletCredentialRepo getWalletCredentialRepo() {
        if (walletCredentialRepo == null) {
            synchronized (this) {
                if (walletCredentialRepo == null) {
                    walletCredentialRepo = new WalletCredentialPostgresRepo(databaseConfig.getPostgresConnection());
                }
            }
        }
        return walletCredentialRepo;
    }

    public TransactionRepo getTransactionRepo() {
        if (transactionRepo == null) {
            synchronized (this) {
                if (transactionRepo == null) {
                    transactionRepo = new TransactionPostgresRepo(databaseConfig.getPostgresConnection());
                }
            }
        }
        return transactionRepo;
    }

    public OtpRepo getOtpRepo() {
        if (otpRepo == null) {
            synchronized (this) {
                if (otpRepo == null) {
                    otpRepo = new OtpPostgresRepo(databaseConfig.getPostgresConnection());
                }
            }
        }
        return otpRepo;
    }
}