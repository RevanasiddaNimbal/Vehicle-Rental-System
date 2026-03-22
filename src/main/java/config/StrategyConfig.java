package config;

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
import wallet.stretegy.PostRegisterationStrategy;
import wallet.stretegy.WalletSetUpStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StrategyConfig {

    private final ServiceConfig serviceConfig;
    private final Scanner input;

    private Map<Integer, VehicleCreator> vehicleCreators;
    private Map<Class<? extends Vehicle>, VehicleUpdater> vehicleUpdaters;
    private PostRegisterationStrategy walletStrategy;

    public StrategyConfig(ServiceConfig serviceConfig, Scanner input) {
        this.serviceConfig = serviceConfig;
        this.input = input;
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
            walletStrategy = new WalletSetUpStrategy(input, serviceConfig.getWalletService(), serviceConfig.getWalletCredentialService());
        }
        return walletStrategy;
    }
}