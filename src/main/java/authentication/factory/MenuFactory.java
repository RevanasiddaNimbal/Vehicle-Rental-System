package authentication.factory;

import UI.*;
import authentication.model.UserRole;

import java.util.Scanner;

public class MenuFactory {
    private final Scanner input;
    private final WalletManagementMenu walletMenu;
    private final CustomerHistoryMenu customerHistoryMenu;
    private final CustomerRentalsMenu customerRentalsMenu;
    private final CustomerAccountMenu customerAccountManagementMenu;
    private final VehicleOwnerAccountMenu vehicleOwnerAccountMenu;
    private final VehicleOwnerRentalsMenu vehicleOwnerRentalsMenu;
    private final VehicleOwnerHistoryMenu vehicleOwnerHistoryMenu;
    private final AdminAccountMenu adminAccountMenu;
    private final AdminsVehicleMenu adminsVehicleMenu;
    private final AdminsCustomerMenu adminsCustomerMenu;
    private final AdminOwnersManu adminOwnersManu;
    private final AdminRentalMenu adminRentalMenu;
    private final VehicleOwnerVehiclesMenu vehiclesMenu;

    public MenuFactory(
            Scanner input,
            WalletManagementMenu walletMenu,
            CustomerHistoryMenu customerHistoryMenu,
            CustomerRentalsMenu customerRentalsMenu,
            CustomerAccountMenu customerAccountManagementMenu,
            VehicleOwnerAccountMenu vehicleOwnerAccountMenu,
            VehicleOwnerRentalsMenu vehicleOwnerRentalsMenu,
            VehicleOwnerHistoryMenu vehicleOwnerHistoryMenu,
            AdminAccountMenu adminAccountMenu,
            AdminsVehicleMenu adminsVehicleMenu,
            AdminsCustomerMenu adminsCustomerMenu,
            AdminOwnersManu adminOwnersManu,
            AdminRentalMenu adminRentalMenu,
            VehicleOwnerVehiclesMenu vehiclesMenu
    ) {
        this.input = input;
        this.walletMenu = walletMenu;
        this.customerHistoryMenu = customerHistoryMenu;
        this.customerRentalsMenu = customerRentalsMenu;
        this.customerAccountManagementMenu = customerAccountManagementMenu;
        this.vehicleOwnerAccountMenu = vehicleOwnerAccountMenu;
        this.vehicleOwnerRentalsMenu = vehicleOwnerRentalsMenu;
        this.vehicleOwnerHistoryMenu = vehicleOwnerHistoryMenu;
        this.adminAccountMenu = adminAccountMenu;
        this.adminsVehicleMenu = adminsVehicleMenu;
        this.adminsCustomerMenu = adminsCustomerMenu;
        this.adminOwnersManu = adminOwnersManu;
        this.adminRentalMenu = adminRentalMenu;
        this.vehiclesMenu = vehiclesMenu;
    }

    public void showMenu(UserRole role, String userId) {
        UsersMenu menu = switch (role) {
            case ADMIN ->
                    new AdminMenu(input, adminOwnersManu, adminsVehicleMenu, adminsCustomerMenu, adminRentalMenu, adminAccountMenu, walletMenu);
            case OWNER ->
                    new VehicleOwnerMenu(input, vehicleOwnerRentalsMenu, vehicleOwnerAccountMenu, vehicleOwnerHistoryMenu, walletMenu, vehiclesMenu);
            case CUSTOMER ->
                    new CustomerMenu(input, customerRentalsMenu, walletMenu, customerHistoryMenu, customerAccountManagementMenu);
        };

        menu.show(userId);
    }
}
