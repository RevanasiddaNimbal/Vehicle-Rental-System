package config;

import admin.service.AdminService;
import authentication.strategy.*;
import cancellation.model.PolicyType;
import cancellation.strategy.CancellationStrategy;
import cancellation.strategy.FullRefundStrategy;
import customer.service.CustomerService;
import otp.service.OtpService;
import user.model.UserRole;
import user.resolver.AdminUserResolver;
import user.resolver.CustomerUserResolver;
import user.resolver.OwnerUserResolver;
import user.resolver.UserTypeResolver;
import vehicle.creator.AutoCreator;
import vehicle.creator.BikeCreator;
import vehicle.creator.CarCreator;
import vehicle.creator.VehicleCreator;
import vehicle.models.Auto;
import vehicle.models.Bike;
import vehicle.models.Car;
import vehicle.models.Vehicle;
import vehicle.updater.AutoUpdater;
import vehicle.updater.BikeUpdater;
import vehicle.updater.CarUpdater;
import vehicle.updater.VehicleUpdater;
import vehicleowner.service.VehicleOwnerService;
import wallet.service.WalletCredentialService;
import wallet.service.WalletService;
import wallet.strategy.PostRegisterationStrategy;
import wallet.strategy.WalletSetUpStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StrategyConfig {

    private final Scanner input;
    private final WalletService walletService;
    private final WalletCredentialService walletCredentialService;
    private final CustomerService customerService;
    private final VehicleOwnerService ownerService;
    private final AdminService adminService;
    private final OtpService otpService;

    private Map<Integer, VehicleCreator> vehicleCreators;
    private Map<Class<? extends Vehicle>, VehicleUpdater> vehicleUpdaters;
    private Map<String, UserTypeResolver> resolverRegistries;
    private PostRegisterationStrategy walletStrategy;
    private Map<UserRole, AuthLoginStretegy> loginStrategies;
    private Map<UserRole, AuthRegisterStrategy> registerStrategies;
    private Map<UserRole, PasswordRecoveryStrategy> passwordRecoveryStrategies;
    private Map<PolicyType, CancellationStrategy> cancellationStrategies;


    public StrategyConfig(Scanner input, WalletService walletService, WalletCredentialService walletCredentialService, CustomerService customerService, VehicleOwnerService ownerService, AdminService adminService, OtpService otpService) {
        this.input = input;
        this.walletService = walletService;
        this.walletCredentialService = walletCredentialService;
        this.customerService = customerService;
        this.ownerService = ownerService;
        this.adminService = adminService;
        this.otpService = otpService;
    }

    public Map<Integer, VehicleCreator> getVehicleCreators() {
        if (vehicleCreators == null) {
            vehicleCreators = new HashMap<>();
            vehicleCreators.put(1, new BikeCreator());
            vehicleCreators.put(2, new CarCreator());
            vehicleCreators.put(3, new AutoCreator());
        }
        return vehicleCreators;
    }

    public Map<Class<? extends Vehicle>, VehicleUpdater> getVehicleUpdaters() {
        if (vehicleUpdaters == null) {
            vehicleUpdaters = new HashMap<>();
            vehicleUpdaters.put(Bike.class, new BikeUpdater());
            vehicleUpdaters.put(Car.class, new CarUpdater());
            vehicleUpdaters.put(Auto.class, new AutoUpdater());
        }
        return vehicleUpdaters;
    }

    public PostRegisterationStrategy getWalletStrategy() {
        if (walletStrategy == null) {
            walletStrategy = new WalletSetUpStrategy(input, walletService, walletCredentialService);
        }
        return walletStrategy;
    }

    public Map<String, UserTypeResolver> getResolverRegistries() {
        if (resolverRegistries == null) {
            resolverRegistries = new HashMap<>();
            resolverRegistries.put("CUS", new CustomerUserResolver(customerService));
            resolverRegistries.put("OWN", new OwnerUserResolver(ownerService));
            resolverRegistries.put("ADM", new AdminUserResolver(adminService));
        }
        return resolverRegistries;
    }

    public Map<UserRole, AuthLoginStretegy> getLoginStrategies() {
        if (loginStrategies == null) {
            loginStrategies = new HashMap<>();
            loginStrategies.put(UserRole.ADMIN, new AdminAuthStretegy(input, adminService, otpService));
            loginStrategies.put(UserRole.OWNER, new OwnerAuthStrategy(input, ownerService, otpService));
            loginStrategies.put(UserRole.CUSTOMER, new CustomerAuthStretegy(input, customerService, otpService));
        }
        return loginStrategies;
    }

    public Map<UserRole, PasswordRecoveryStrategy> getPasswordRecoveryStrategies() {
        if (passwordRecoveryStrategies == null) {
            passwordRecoveryStrategies = new HashMap<>();
            passwordRecoveryStrategies.put(UserRole.ADMIN, new AdminAuthStretegy(input, adminService, otpService));
            passwordRecoveryStrategies.put(UserRole.OWNER, new OwnerAuthStrategy(input, ownerService, otpService));
            passwordRecoveryStrategies.put(UserRole.CUSTOMER, new CustomerAuthStretegy(input, customerService, otpService));
        }
        return passwordRecoveryStrategies;
    }

    public Map<UserRole, AuthRegisterStrategy> getRegisterStrategies() {
        if (registerStrategies == null) {
            registerStrategies = new HashMap<>();
            registerStrategies.put(UserRole.OWNER, new OwnerAuthStrategy(input, ownerService, otpService));
            registerStrategies.put(UserRole.CUSTOMER, new CustomerAuthStretegy(input, customerService, otpService));
        }
        return registerStrategies;
    }

    public Map<PolicyType, CancellationStrategy> getCancellationStrategies() {
        if (cancellationStrategies == null) {
            cancellationStrategies = new HashMap<>();
            cancellationStrategies.put(PolicyType.FULL_REFUND, new FullRefundStrategy());
            cancellationStrategies.put(PolicyType.PARTIAL_REFUND, new cancellation.strategy.PartialRefundStrategy());
            cancellationStrategies.put(PolicyType.NO_REFUND, new cancellation.strategy.NoRefundStrategy());
        }
        return cancellationStrategies;
    }

}