package config;

import UI.*;
import authentication.factory.MenuFactory;

import java.util.Scanner;

public class MenuConfig {

    private final ControllerConfig controllerConfig;
    private final Scanner input;

    private volatile WalletManagementMenu walletManagementMenu;
    private volatile CustomerHistoryMenu customerHistoryMenu;
    private volatile CustomerRentalsMenu customerRentalsMenu;
    private volatile CustomerAccountMenu customerAccountMenu;
    private volatile VehicleOwnerHistoryMenu vehicleOwnerHistoryMenu;
    private volatile VehicleOwnerRentalsMenu vehicleOwnerRentalsMenu;
    private volatile VehicleOwnerAccountMenu vehicleOwnerAccountMenu;
    private volatile VehicleOwnerVehiclesMenu vehicleOwnerVehiclesMenu;
    private volatile AdminAccountMenu adminAccountMenu;
    private volatile AdminsVehicleMenu adminsVehicleMenu;
    private volatile AdminsCustomerMenu adminsCustomerMenu;
    private volatile AdminOwnersManu adminOwnersManu;
    private volatile AdminRentalMenu adminRentalMenu;
    private volatile MenuFactory menuFactory;
    private volatile TransactionMenu transactionMenu;

    public MenuConfig(ControllerConfig controllerConfig, Scanner input) {
        this.controllerConfig = controllerConfig;
        this.input = input;
    }

    public WalletManagementMenu getWalletManagementMenu() {
        if (walletManagementMenu == null) {
            synchronized (this) {
                if (walletManagementMenu == null) {
                    walletManagementMenu =
                            new WalletManagementMenu(
                                    input,
                                    controllerConfig.getWalletController(),
                                    controllerConfig.getWalletCredentialController()
                            );
                }
            }
        }
        return walletManagementMenu;
    }

    public CustomerHistoryMenu getCustomerHistoryMenu() {
        if (customerHistoryMenu == null) {
            synchronized (this) {
                if (customerHistoryMenu == null) {
                    customerHistoryMenu =
                            new CustomerHistoryMenu(
                                    input,
                                    controllerConfig.getRentalController(),
                                    controllerConfig.getPenaltyController(),
                                    controllerConfig.getCancellationController()
                            );
                }
            }
        }
        return customerHistoryMenu;
    }

    public CustomerRentalsMenu getCustomerRentalsMenu() {
        if (customerRentalsMenu == null) {
            synchronized (this) {
                if (customerRentalsMenu == null) {
                    customerRentalsMenu =
                            new CustomerRentalsMenu(
                                    input,
                                    controllerConfig.getVehicleController(),
                                    controllerConfig.getRentalController()
                            );
                }
            }
        }
        return customerRentalsMenu;
    }

    public CustomerAccountMenu getCustomerAccountMenu() {
        if (customerAccountMenu == null) {
            synchronized (this) {
                if (customerAccountMenu == null) {
                    customerAccountMenu =
                            new CustomerAccountMenu(
                                    input,
                                    controllerConfig.getCustomerController()
                            );
                }
            }
        }
        return customerAccountMenu;
    }

    public VehicleOwnerHistoryMenu getVehicleOwnerHistoryMenu() {
        if (vehicleOwnerHistoryMenu == null) {
            synchronized (this) {
                if (vehicleOwnerHistoryMenu == null) {
                    vehicleOwnerHistoryMenu =
                            new VehicleOwnerHistoryMenu(
                                    input,
                                    controllerConfig.getRentalController(),
                                    controllerConfig.getCancellationController()
                            );
                }
            }
        }
        return vehicleOwnerHistoryMenu;
    }

    public VehicleOwnerRentalsMenu getVehicleOwnerRentalsMenu() {
        if (vehicleOwnerRentalsMenu == null) {
            synchronized (this) {
                if (vehicleOwnerRentalsMenu == null) {
                    vehicleOwnerRentalsMenu =
                            new VehicleOwnerRentalsMenu(
                                    input,
                                    controllerConfig.getRentalController()
                            );
                }
            }
        }
        return vehicleOwnerRentalsMenu;
    }

    public VehicleOwnerAccountMenu getVehicleOwnerAccountMenu() {
        if (vehicleOwnerAccountMenu == null) {
            synchronized (this) {
                if (vehicleOwnerAccountMenu == null) {
                    vehicleOwnerAccountMenu =
                            new VehicleOwnerAccountMenu(
                                    input,
                                    controllerConfig.getVehicleOwnerController()
                            );
                }
            }
        }
        return vehicleOwnerAccountMenu;
    }

    public VehicleOwnerVehiclesMenu getVehicleOwnerVehiclesMenu() {
        if (vehicleOwnerVehiclesMenu == null) {
            synchronized (this) {
                if (vehicleOwnerVehiclesMenu == null) {
                    vehicleOwnerVehiclesMenu =
                            new VehicleOwnerVehiclesMenu(
                                    input,
                                    controllerConfig.getVehicleController()
                            );
                }
            }
        }
        return vehicleOwnerVehiclesMenu;
    }

    public AdminAccountMenu getAdminAccountMenu() {
        if (adminAccountMenu == null) {
            synchronized (this) {
                if (adminAccountMenu == null) {
                    adminAccountMenu =
                            new AdminAccountMenu(
                                    input,
                                    controllerConfig.getAdminController()
                            );
                }
            }
        }
        return adminAccountMenu;
    }

    public AdminsVehicleMenu getAdminsVehicleMenu() {
        if (adminsVehicleMenu == null) {
            synchronized (this) {
                if (adminsVehicleMenu == null) {
                    adminsVehicleMenu =
                            new AdminsVehicleMenu(
                                    input,
                                    controllerConfig.getVehicleController()
                            );
                }
            }
        }
        return adminsVehicleMenu;
    }

    public AdminsCustomerMenu getAdminsCustomerMenu() {
        if (adminsCustomerMenu == null) {
            synchronized (this) {
                if (adminsCustomerMenu == null) {
                    adminsCustomerMenu =
                            new AdminsCustomerMenu(
                                    input,
                                    controllerConfig.getCustomerController()
                            );
                }
            }
        }
        return adminsCustomerMenu;
    }

    public AdminOwnersManu getAdminOwnersManu() {
        if (adminOwnersManu == null) {
            synchronized (this) {
                if (adminOwnersManu == null) {
                    adminOwnersManu =
                            new AdminOwnersManu(
                                    input,
                                    controllerConfig.getVehicleOwnerController()
                            );
                }
            }
        }
        return adminOwnersManu;
    }

    public AdminRentalMenu getAdminRentalMenu() {
        if (adminRentalMenu == null) {
            synchronized (this) {
                if (adminRentalMenu == null) {
                    adminRentalMenu =
                            new AdminRentalMenu(
                                    input,
                                    controllerConfig.getRentalController(),
                                    controllerConfig.getPenaltyController(),
                                    controllerConfig.getCancellationController()
                            );
                }
            }
        }
        return adminRentalMenu;
    }

    public MenuFactory getMenuFactory() {
        if (menuFactory == null) {
            synchronized (this) {
                if (menuFactory == null) {
                    menuFactory = new MenuFactory(input, this);
                }
            }
        }
        return menuFactory;
    }

    public TransactionMenu getTransactionMenu() {
        if (transactionMenu == null) {
            synchronized (this) {
                if (transactionMenu == null) {
                    transactionMenu =
                            new TransactionMenu(
                                    input,
                                    controllerConfig.getTransactionController()
                            );
                }
            }
        }
        return transactionMenu;
    }
}