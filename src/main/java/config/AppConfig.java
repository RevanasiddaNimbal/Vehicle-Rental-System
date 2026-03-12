package config;

import UI.AuthMenu;
import UI.Documentation;
import UI.Menu;
import application.Application;
import authentication.controller.AuthController;
import authentication.factory.AuthStrategyFactory;
import authentication.factory.MenuFactory;
import authentication.service.AuthService;
import vehicle.Controller.VehicleController;
import vehicle.Creator.AutoCreator;
import vehicle.Creator.BikeCreator;
import vehicle.Creator.CarCreator;
import vehicle.Creator.VehicleCreator;
import vehicle.Models.Auto;
import vehicle.Models.Bike;
import vehicle.Models.Car;
import vehicle.Models.Vehicle;
import vehicle.Repository.MemoryVehicleRepo;
import vehicle.Repository.VehicleRepo;
import vehicle.Service.VehicleService;
import vehicle.Updater.AutoUpdater;
import vehicle.Updater.BikeUpdater;
import vehicle.Updater.CarUpdater;
import vehicle.Updater.VehicleUpdater;
import vehicleowner.Controller.VehicleOwnerController;
import vehicleowner.Repository.MemoryVehicleOwnerRepo;
import vehicleowner.Repository.VehicleOwnerRepo;
import vehicleowner.Service.VehicleOwnerService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AppConfig {
    public static Application createApplication(Scanner input) {
        // documentation layer.
        Menu documentation = new Documentation();

        // Vehicle layer.
        Map<Integer, VehicleCreator> creators = new HashMap<>();
        creators.put(1, new BikeCreator());
        creators.put(2, new CarCreator());
        creators.put(3, new AutoCreator());

        Map<Class<? extends Vehicle>, VehicleUpdater> updaters = new HashMap<>();
        updaters.put(Bike.class, new BikeUpdater());
        updaters.put(Car.class, new CarUpdater());
        updaters.put(Auto.class, new AutoUpdater());

        VehicleRepo vehicleRepo = new MemoryVehicleRepo();
        VehicleService vehicleService = new VehicleService(vehicleRepo);
        VehicleController vehicleController = new VehicleController(vehicleService, creators, updaters);

        // Vehicle-Owner layer.
        VehicleOwnerRepo ownerRepo = new MemoryVehicleOwnerRepo();
        VehicleOwnerService ownerService = new VehicleOwnerService(ownerRepo);
        VehicleOwnerController ownerController = new VehicleOwnerController(ownerService);

        // AuthMenu layer.
        AuthStrategyFactory authStrategyFactory = new AuthStrategyFactory(input, ownerService);
        MenuFactory menuFactory = new MenuFactory(input, ownerController, vehicleController);
        AuthService authService = new AuthService(authStrategyFactory);
        AuthController authController = new AuthController(input, authService, menuFactory);
        Menu authMenu = new AuthMenu(input, authController);

        return new Application(input, documentation, authMenu);
    }
}
