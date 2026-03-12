package authentication.factory;

import authentication.model.UserRole;
import authentication.strategy.AuthStrategy;
import authentication.strategy.OwnerAuthStrategy;
import vehicleowner.Service.VehicleOwnerService;

import java.util.Scanner;

public class AuthStrategyFactory {
    private final VehicleOwnerService ownerService;
    private final Scanner input;

    public AuthStrategyFactory(Scanner input, VehicleOwnerService ownerService) {
        this.input = input;
        this.ownerService = ownerService;
    }

    public AuthStrategy getStrategy(UserRole role) {
        switch (role) {
            case OWNER:
                return new OwnerAuthStrategy(input, ownerService);
            default:
                System.out.println("Invalid role");
                return null;
        }
    }
}
