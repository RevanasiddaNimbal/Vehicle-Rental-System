package authentication.factory;

import UI.AdminMenu;
import UI.CustomerMenu;
import UI.Menu;
import UI.VehicleOwnerMenu;
import authentication.model.UserRole;
import vehicle.Controller.VehicleController;
import vehicleowner.Controller.VehicleOwnerController;

import java.util.Scanner;

public class MenuFactory {
    private final Scanner input;
    private final VehicleOwnerController ownerController;
    private final VehicleController vehicleController;

    public MenuFactory(Scanner input, VehicleOwnerController ownerController, VehicleController vehicleController) {
        this.input = input;
        this.ownerController = ownerController;
        this.vehicleController = vehicleController;
    }

    public void showMenu(UserRole role) {
        Menu menu = switch (role) {
            case ADMIN -> new AdminMenu(input, ownerController, vehicleController);
            case OWNER -> new VehicleOwnerMenu(input, vehicleController, ownerController);
            case CUSTOMER -> new CustomerMenu(input);
        };

        menu.show(role); // execute menu
    }
}
