package config;

import UI.*;
import authentication.factory.MenuFactory;

import java.util.Scanner;

public class MenuConfig {

    private final ControllerConfig controllerConfig;
    private final Scanner input;

    private WalletManagementMenu walletManagementMenu;
    private CustomerHistoryMenu customerHistoryMenu;
    private CustomerRentalsMenu customerRentalsMenu;
    private CustomerAccountMenu customerAccountMenu;
    private VehicleOwnerHistoryMenu vehicleOwnerHistoryMenu;
    private VehicleOwnerRentalsMenu vehicleOwnerRentalsMenu;
    private VehicleOwnerAccountMenu vehicleOwnerAccountMenu;
    private VehicleOwnerVehiclesMenu vehicleOwnerVehiclesMenu;
    private AdminAccountMenu adminAccountMenu;
    private AdminsVehicleMenu adminsVehicleMenu;
    private AdminsCustomerMenu adminsCustomerMenu;
    private AdminOwnersManu adminOwnersManu;
    private AdminRentalMenu adminRentalMenu;
    private MenuFactory menuFactory;
    private TransactionMenu transactionMenu;

    public MenuConfig(ControllerConfig controllerConfig, Scanner input) {
        this.controllerConfig = controllerConfig;
        this.input = input;
    }

    public WalletManagementMenu getWalletManagementMenu() {
        if (walletManagementMenu == null) {
            walletManagementMenu = new WalletManagementMenu(input, controllerConfig.getWalletController(), controllerConfig.getWalletCredentialController());
        }
        return walletManagementMenu;
    }

    public CustomerHistoryMenu getCustomerHistoryMenu() {
        if (customerHistoryMenu == null) {
            customerHistoryMenu = new CustomerHistoryMenu(input, controllerConfig.getRentalController(), controllerConfig.getPenaltyController(), controllerConfig.getCancellationController());
        }
        return customerHistoryMenu;
    }

    public CustomerRentalsMenu getCustomerRentalsMenu() {
        if (customerRentalsMenu == null) {
            customerRentalsMenu = new CustomerRentalsMenu(input, controllerConfig.getVehicleController(), controllerConfig.getRentalController());
        }
        return customerRentalsMenu;
    }

    public CustomerAccountMenu getCustomerAccountMenu() {
        if (customerAccountMenu == null) {
            customerAccountMenu = new CustomerAccountMenu(input, controllerConfig.getCustomerController());
        }
        return customerAccountMenu;
    }

    public VehicleOwnerHistoryMenu getVehicleOwnerHistoryMenu() {
        if (vehicleOwnerHistoryMenu == null) {
            vehicleOwnerHistoryMenu = new VehicleOwnerHistoryMenu(input, controllerConfig.getRentalController(), controllerConfig.getCancellationController());
        }
        return vehicleOwnerHistoryMenu;
    }

    public VehicleOwnerRentalsMenu getVehicleOwnerRentalsMenu() {
        if (vehicleOwnerRentalsMenu == null) {
            vehicleOwnerRentalsMenu = new VehicleOwnerRentalsMenu(input, controllerConfig.getRentalController());
        }
        return vehicleOwnerRentalsMenu;
    }

    public VehicleOwnerAccountMenu getVehicleOwnerAccountMenu() {
        if (vehicleOwnerAccountMenu == null) {
            vehicleOwnerAccountMenu = new VehicleOwnerAccountMenu(input, controllerConfig.getVehicleOwnerController());
        }
        return vehicleOwnerAccountMenu;
    }

    public VehicleOwnerVehiclesMenu getVehicleOwnerVehiclesMenu() {
        if (vehicleOwnerVehiclesMenu == null) {
            vehicleOwnerVehiclesMenu = new VehicleOwnerVehiclesMenu(input, controllerConfig.getVehicleController());
        }
        return vehicleOwnerVehiclesMenu;
    }

    public AdminAccountMenu getAdminAccountMenu() {
        if (adminAccountMenu == null) {
            adminAccountMenu = new AdminAccountMenu(input, controllerConfig.getAdminController());
        }
        return adminAccountMenu;
    }

    public AdminsVehicleMenu getAdminsVehicleMenu() {
        if (adminsVehicleMenu == null) {
            adminsVehicleMenu = new AdminsVehicleMenu(input, controllerConfig.getVehicleController());
        }
        return adminsVehicleMenu;
    }

    public AdminsCustomerMenu getAdminsCustomerMenu() {
        if (adminsCustomerMenu == null) {
            adminsCustomerMenu = new AdminsCustomerMenu(input, controllerConfig.getCustomerController());
        }
        return adminsCustomerMenu;
    }

    public AdminOwnersManu getAdminOwnersManu() {
        if (adminOwnersManu == null) {
            adminOwnersManu = new AdminOwnersManu(input, controllerConfig.getVehicleOwnerController());
        }
        return adminOwnersManu;
    }

    public AdminRentalMenu getAdminRentalMenu() {
        if (adminRentalMenu == null) {
            adminRentalMenu = new AdminRentalMenu(input, controllerConfig.getRentalController(), controllerConfig.getPenaltyController(), controllerConfig.getCancellationController());
        }
        return adminRentalMenu;
    }

    public MenuFactory getMenuFactory() {
        if (menuFactory == null) {
            menuFactory = new MenuFactory(input, this);
        }
        return menuFactory;
    }

    public TransactionMenu getTransactionMenu() {
        if (transactionMenu == null) {
            transactionMenu = new TransactionMenu(input, controllerConfig.getTransactionController());
        }
        return transactionMenu;
    }
}