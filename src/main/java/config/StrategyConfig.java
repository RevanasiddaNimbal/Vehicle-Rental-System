package config;

import admin.service.AdminService;
import customer.service.CustomerService;
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
import wallet.stretegy.PostRegisterationStrategy;
import wallet.stretegy.WalletSetUpStrategy;

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

    private Map<Integer, VehicleCreator> vehicleCreators;
    private Map<Class<? extends Vehicle>, VehicleUpdater> vehicleUpdaters;
    private Map<String, UserTypeResolver> resolverRegistries;
    private PostRegisterationStrategy walletStrategy;


    public StrategyConfig(Scanner input, WalletService walletService, WalletCredentialService walletCredentialService, CustomerService customerService, VehicleOwnerService ownerService, AdminService adminService) {
        this.input = input;
        this.walletService = walletService;
        this.walletCredentialService = walletCredentialService;
        this.customerService = customerService;
        this.ownerService = ownerService;
        this.adminService = adminService;
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

}