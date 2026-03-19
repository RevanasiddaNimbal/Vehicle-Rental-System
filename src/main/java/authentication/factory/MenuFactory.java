package authentication.factory;

import UI.AdminMenu;
import UI.CustomerMenu;
import UI.UsersMenu;
import UI.VehicleOwnerMenu;
import admin.controller.AdminController;
import authentication.model.UserRole;
import customer.controller.CustomerController;
import rental.controller.RentalController;
import vehicle.controller.VehicleController;
import vehicleowner.controller.VehicleOwnerController;

import java.util.Scanner;

public class MenuFactory {
    private final Scanner input;
    private final VehicleOwnerController ownerController;
    private final VehicleController vehicleController;
    private final CustomerController customerController;
    private final AdminController adminController;
    private final RentalController rentalController;

    public MenuFactory(Scanner input, VehicleOwnerController ownerController, VehicleController vehicleController, CustomerController customerController, AdminController adminController, RentalController rentalController) {
        this.input = input;
        this.ownerController = ownerController;
        this.vehicleController = vehicleController;
        this.customerController = customerController;
        this.adminController = adminController;
        this.rentalController = rentalController;
    }

    public void showMenu(UserRole role, String userId) {
        UsersMenu menu = switch (role) {
            case ADMIN ->
                    new AdminMenu(input, ownerController, vehicleController, customerController, adminController, rentalController);
            case OWNER -> new VehicleOwnerMenu(input, vehicleController, ownerController, rentalController);
            case CUSTOMER -> new CustomerMenu(input, customerController, rentalController, vehicleController);
        };

        menu.show(role, userId);
    }
}
