package authentication.factory;

import UI.AdminMenu;
import UI.CustomerMenu;
import UI.UsersMenu;
import UI.VehicleOwnerMenu;
import admin.controller.AdminController;
import authentication.model.UserRole;
import customer.controller.CustomerController;
import vehicle.controller.VehicleController;
import vehicleowner.controller.VehicleOwnerController;

import java.util.Scanner;

public class MenuFactory {
    private final Scanner input;
    private final VehicleOwnerController ownerController;
    private final VehicleController vehicleController;
    private final CustomerController customerController;
    private final AdminController adminController;

    public MenuFactory(Scanner input, VehicleOwnerController ownerController, VehicleController vehicleController, CustomerController customerController, AdminController adminController) {
        this.input = input;
        this.ownerController = ownerController;
        this.vehicleController = vehicleController;
        this.customerController = customerController;
        this.adminController = adminController;
    }

    public void showMenu(UserRole role, String ownerId) {
        UsersMenu menu = switch (role) {
            case ADMIN -> new AdminMenu(input, ownerController, vehicleController, customerController, adminController);
            case OWNER -> new VehicleOwnerMenu(input, vehicleController, ownerController);
            case CUSTOMER -> new CustomerMenu(input, customerController);
        };

        menu.show(role, ownerId);
    }
}
