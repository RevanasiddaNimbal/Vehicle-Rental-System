package authentication.factory;

import UI.AdminMenu;
import UI.CustomerMenu;
import UI.UsersMenu;
import UI.VehicleOwnerMenu;
import authentication.model.UserRole;
import config.MenuConfig;

import java.util.Scanner;

public class MenuFactory {
    private final Scanner input;
    private final MenuConfig menuConfig;

    public MenuFactory(Scanner input, MenuConfig menuConfig) {
        this.input = input;
        this.menuConfig = menuConfig;
    }

    public void showMenu(UserRole role, String userId) {
        UsersMenu menu = switch (role) {
            case ADMIN -> new AdminMenu(
                    input,
                    menuConfig.getAdminOwnersManu(),
                    menuConfig.getAdminsVehicleMenu(),
                    menuConfig.getAdminsCustomerMenu(),
                    menuConfig.getAdminRentalMenu(),
                    menuConfig.getAdminAccountMenu(),
                    menuConfig.getWalletManagementMenu(),
                    menuConfig.getTransactionMenu()
            );
            case OWNER -> new VehicleOwnerMenu(
                    input,
                    menuConfig.getVehicleOwnerRentalsMenu(),
                    menuConfig.getVehicleOwnerAccountMenu(),
                    menuConfig.getVehicleOwnerHistoryMenu(),
                    menuConfig.getWalletManagementMenu(),
                    menuConfig.getVehicleOwnerVehiclesMenu(),
                    menuConfig.getTransactionMenu()
            );
            case CUSTOMER -> new CustomerMenu(
                    input,
                    menuConfig.getCustomerRentalsMenu(),
                    menuConfig.getWalletManagementMenu(),
                    menuConfig.getCustomerHistoryMenu(),
                    menuConfig.getCustomerAccountMenu(),
                    menuConfig.getTransactionMenu()
            );
        };
        menu.show(userId);
    }
}