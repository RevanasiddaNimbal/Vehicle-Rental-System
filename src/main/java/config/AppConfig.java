package config;

import UI.*;
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
import customer.model.Customer;
import customer.repository.CustomerRepo;
import customer.repository.CustomersPostgresRepo;
import customer.service.CustomerService;
import database.DatabaseConnection;
import database.PostgresConnection;
import invoice.renders.InvoiceConsoleRender;
import invoice.renders.InvoiceRender;
import invoice.service.InvoiceService;
import notification.provider.BrevoEmailProvider;
import notification.service.EmailService;
import penalty.controller.PenaltyController;
import penalty.factory.PenaltyStrategyFactory;
import penalty.model.Penalty;
import penalty.repository.PenaltyPostgresRepo;
import penalty.repository.PenaltyRepo;
import penalty.service.PenaltyService;
import rental.billing.RentalPriceCalculator;
import rental.billing.RentalTimeCalculator;
import rental.controller.RentalController;
import rental.model.Rental;
import rental.repository.RentalRepo;
import rental.repository.RentalsPostgresRepo;
import rental.service.RentalService;
import rental.stretegy.BasePriceStrategy;
import rental.stretegy.DiscountStrategy;
import rental.stretegy.PricingStrategy;
import rental.stretegy.WeekendStrategy;
import vehicle.controller.VehicleController;
import vehicle.creator.AutoCreator;
import vehicle.creator.BikeCreator;
import vehicle.creator.CarCreator;
import vehicle.creator.VehicleCreator;
import vehicle.models.Auto;
import vehicle.models.Bike;
import vehicle.models.Car;
import vehicle.models.Vehicle;
import vehicle.repository.VehicleRepo;
import vehicle.repository.VehiclesPostgresRepo;
import vehicle.service.VehicleService;
import vehicle.updater.AutoUpdater;
import vehicle.updater.BikeUpdater;
import vehicle.updater.CarUpdater;
import vehicle.updater.VehicleUpdater;
import vehicleowner.controller.VehicleOwnerController;
import vehicleowner.models.VehicleOwner;
import vehicleowner.repository.VehicleOwnerRepo;
import vehicleowner.repository.VehicleOwnersPostgresRepo;
import vehicleowner.service.VehicleOwnerService;

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

        //Printer layer
        UserPrinter<Customer> customerPrinter = new CustomerPrinter();
        UserPrinter<Vehicle> vehiclePrinter = new VehiclePrinter();
        UserPrinter<VehicleOwner> ownerPrinter = new OwnersPrinter();
        UserPrinter<Rental> rentalPrinter = new RentalPrinter();
        UserPrinter<Penalty> penaltyPrinter = new PenaltyPrinter();

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
        VehicleController vehicleController = new VehicleController(vehicleService, creators, updaters, vehiclePrinter);

        // Admin layer
        AdminRepo adminRepo = new AdminPostgresRepo(postgresConnection);
        AdminService adminService = new AdminService(adminRepo);
        AdminController adminController = new AdminController(adminService);

        // Vehicle-Owner layer.
        VehicleOwnerRepo ownerRepo = new VehicleOwnersPostgresRepo(postgresConnection);
        VehicleOwnerService ownerService = new VehicleOwnerService(ownerRepo);
        VehicleOwnerController ownerController = new VehicleOwnerController(ownerService, ownerPrinter);

        //Customer Layer
        CustomerRepo customerRepo = new CustomersPostgresRepo(postgresConnection);
        CustomerService customerService = new CustomerService(customerRepo);
        CustomerController customerController = new CustomerController(customerService, customerPrinter);

        // Invoice layer
        InvoiceRender invoiceRender = new InvoiceConsoleRender();
        InvoiceService invoiceService = new InvoiceService(invoiceRender);

        // rental calculators
        Map<Integer, PricingStrategy> pricingStrategies = new HashMap<>();
        pricingStrategies.put(1, new BasePriceStrategy());
        pricingStrategies.put(2, new WeekendStrategy());
        pricingStrategies.put(3, new DiscountStrategy());
        RentalPriceCalculator rentalPriceCalculator = new RentalPriceCalculator(pricingStrategies);
        RentalTimeCalculator rentalTimeCalculator = new RentalTimeCalculator();

        // Penalty layer
        PenaltyStrategyFactory penaltyStrategyFactory = new PenaltyStrategyFactory(rentalTimeCalculator);
        PenaltyRepo penaltyRepo = new PenaltyPostgresRepo(postgresConnection);
        PenaltyService penaltyService = new PenaltyService(penaltyRepo, penaltyStrategyFactory);
        PenaltyController penaltyController = new PenaltyController(penaltyService, penaltyPrinter);

        //rental layer
        RentalRepo rentalPostgresRepo = new RentalsPostgresRepo(postgresConnection);
        RentalService rentalService = new RentalService(rentalPostgresRepo, rentalPriceCalculator, vehicleService, ownerService, customerService, rentalTimeCalculator);
        RentalController rentalController = new RentalController(rentalService, vehicleService, invoiceService, customerService, penaltyService, rentalPrinter, customerPrinter, ownerPrinter, vehiclePrinter, penaltyPrinter);

        //Mailer Layer
        EmailServiceConfig mailConfig = new EmailServiceConfig();
        EmailService mailService = new BrevoEmailProvider(mailConfig);

        //Otp layer
        OtpStorage otpStorage = new OtpStorage();
        OtpService otpService = new OtpService(otpStorage, mailService);

        // AuthMenu layer.
        AuthStrategyFactory authStrategyFactory = new AuthStrategyFactory(input, ownerService, adminService, customerService, otpService);
        MenuFactory menuFactory = new MenuFactory(input, ownerController, vehicleController, customerController, adminController, rentalController, penaltyController);
        AuthService authService = new AuthService(authStrategyFactory);
        AuthController authController = new AuthController(input, authService, menuFactory);
        UserRoleMenu authMenu = new AuthMenu(input, authController);


        return new Application(input, documentation, authMenu);
    }
}
