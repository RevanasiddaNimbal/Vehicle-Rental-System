package config;

import UI.AuthMenu;
import UI.Documentation;
import UI.UserRoleMenu;
import admin.controller.AdminController;
import admin.repository.AdminPostgresRepo;
import admin.repository.AdminRepo;
import admin.service.AdminService;
import application.Application;
import authentication.controller.AuthController;
import authentication.factory.AuthStrategyFactory;
import authentication.factory.MenuFactory;
import authentication.repository.OtpStorage;
import authentication.service.AuthService;
import authentication.service.OtpService;
import customer.controller.CustomerController;
import customer.repository.CustomerRepo;
import customer.repository.CustomersPostgresRepo;
import customer.service.CustomerService;
import database.DatabaseConnection;
import database.PostgresConnection;
import notification.provider.BrevoEmailProvider;
import notification.service.EmailService;
import vehicle.Controller.VehicleController;
import vehicle.Creator.AutoCreator;
import vehicle.Creator.BikeCreator;
import vehicle.Creator.CarCreator;
import vehicle.Creator.VehicleCreator;
import vehicle.Models.Auto;
import vehicle.Models.Bike;
import vehicle.Models.Car;
import vehicle.Models.Vehicle;
import vehicle.Repository.VehicleRepo;
import vehicle.Repository.VehiclesPostgresRepo;
import vehicle.Service.VehicleService;
import vehicle.Updater.AutoUpdater;
import vehicle.Updater.BikeUpdater;
import vehicle.Updater.CarUpdater;
import vehicle.Updater.VehicleUpdater;
import vehicleowner.Controller.VehicleOwnerController;
import vehicleowner.Repository.VehicleOwnerRepo;
import vehicleowner.Repository.VehicleOwnersPostgresRepo;
import vehicleowner.Service.VehicleOwnerService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AppConfig {
    public static Application createApplication(Scanner input) {
        // documentation layer.
        UserRoleMenu documentation = new Documentation();

        // Database layer
        DbConfig dbConfig = new DbConfig();
        DatabaseConnection postgresConnection = new PostgresConnection(dbConfig);

        // Vehicle layer.
        Map<Integer, VehicleCreator> creators = new HashMap<>();
        creators.put(1, new BikeCreator());
        creators.put(2, new CarCreator());
        creators.put(3, new AutoCreator());

        Map<Class<? extends Vehicle>, VehicleUpdater> updaters = new HashMap<>();
        updaters.put(Bike.class, new BikeUpdater());
        updaters.put(Car.class, new CarUpdater());
        updaters.put(Auto.class, new AutoUpdater());

        VehicleRepo vehicleRepo = new VehiclesPostgresRepo(postgresConnection);
        VehicleService vehicleService = new VehicleService(vehicleRepo);
        VehicleController vehicleController = new VehicleController(vehicleService, creators, updaters);

        // Admin layer
        AdminRepo adminRepo = new AdminPostgresRepo(postgresConnection);
        AdminService adminService = new AdminService(adminRepo);
        AdminController adminController = new AdminController(adminService);

        // Vehicle-Owner layer.
        VehicleOwnerRepo ownerRepo = new VehicleOwnersPostgresRepo(postgresConnection);
        VehicleOwnerService ownerService = new VehicleOwnerService(ownerRepo);
        VehicleOwnerController ownerController = new VehicleOwnerController(ownerService);

        //Customer Layer
        CustomerRepo customerRepo = new CustomersPostgresRepo(postgresConnection);
        CustomerService customerService = new CustomerService(customerRepo);
        CustomerController customerController = new CustomerController(customerService);

        //Mailer Layer
        EmailServiceConfig mailConfig = new EmailServiceConfig();
        EmailService mailService = new BrevoEmailProvider(mailConfig);

        //Otp layer
        OtpStorage otpStorage = new OtpStorage();
        OtpService otpService = new OtpService(otpStorage, mailService);

        // AuthMenu layer.
        AuthStrategyFactory authStrategyFactory = new AuthStrategyFactory(input, ownerService, adminService, customerService, otpService);
        MenuFactory menuFactory = new MenuFactory(input, ownerController, vehicleController, customerController, adminController);
        AuthService authService = new AuthService(authStrategyFactory);
        AuthController authController = new AuthController(input, authService, menuFactory);
        UserRoleMenu authMenu = new AuthMenu(input, authController);


        return new Application(input, documentation, authMenu);
    }
}
