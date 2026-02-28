package config;

import UI.*;
import application.Application;
import database.PostgresConnection;
import vehicle.Controller.VehicleController;
import vehicle.Creator.AutoCreator;
import vehicle.Creator.BikeCreator;
import vehicle.Creator.CarCreator;
import vehicle.Creator.VehicleCreator;
import vehicle.Models.Auto;
import vehicle.Models.Bike;
import vehicle.Models.Car;
import vehicle.Models.Vehicle;
import vehicle.Service.VehicleService;
import vehicle.Updater.AutoUpdater;
import vehicle.Updater.BikeUpdater;
import vehicle.Updater.CarUpdater;
import vehicle.Updater.VehicleUpdater;
import vehicle.repository.PostgresVehicleRepo;
import vehicle.repository.VehicleRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AppConfig {
    public static Application createApplication(Scanner input) {

        // Documentation Layer
        Menu documentation = new ShowDocumentation();

        // Vehicle layer
        Map<Integer, VehicleCreator> creators = new HashMap<>();
        creators.put(1, new BikeCreator());
        creators.put(2, new CarCreator());
        creators.put(3, new AutoCreator());

        Map<Class<? extends Vehicle>, VehicleUpdater> updaters = new HashMap<>();
        updaters.put(Bike.class, new BikeUpdater());
        updaters.put(Car.class, new CarUpdater());
        updaters.put(Auto.class, new AutoUpdater());

        DbConfig dbConfig = new DbConfig();
        PostgresConnection connection = new PostgresConnection(dbConfig);
        VehicleRepo postRepository = new PostgresVehicleRepo(connection);
        VehicleService vehicleService = new VehicleService(postRepository);
//        VehicleRepo MemoRepository = new MemoryVehicleRepo();
        VehicleController vehicleController = new VehicleController(vehicleService, creators, updaters);
        Menu vehicleMenu = new VehicleMenu(vehicleController, input);

        //Customer layer
        Menu customerMenu = new CustomerMenu(input);

        // Rental layer
        Menu rentalMenu = new RentalMenu(input);

        // Storing in HashMap
        Map<Integer, Menu> manus = new HashMap<>();
        manus.put(1, documentation);
        manus.put(2, vehicleMenu);
        manus.put(3, customerMenu);
        manus.put(4, rentalMenu);

        return new Application(input, manus);
    }
}
