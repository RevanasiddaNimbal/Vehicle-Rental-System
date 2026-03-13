package authentication.factory;

import admin.service.AdminService;
import authentication.model.UserRole;
import authentication.strategy.AdminAuthStretegy;
import authentication.strategy.AuthLoginStretegy;
import authentication.strategy.AuthRegisterStretegy;
import authentication.strategy.OwnerAuthStrategy;
import vehicleowner.Service.VehicleOwnerService;

import java.util.Scanner;

public class AuthStrategyFactory {
    private final VehicleOwnerService ownerService;
    private final AdminService adminService;
    private final Scanner input;

    public AuthStrategyFactory(Scanner input, VehicleOwnerService ownerService, AdminService adminService) {
        this.input = input;
        this.ownerService = ownerService;
        this.adminService = adminService;
    }

    public AuthLoginStretegy getLoginStrategy(UserRole role) {
        switch (role) {
            case ADMIN:
                return new AdminAuthStretegy(input, adminService);
            case OWNER:
                return new OwnerAuthStrategy(input, ownerService);
            default:
                System.out.println("Invalid role");
                return null;
        }
    }

    public AuthRegisterStretegy getRegisterStrategy(UserRole role) {
        switch (role) {
            case OWNER:
                return new OwnerAuthStrategy(input, ownerService);
            default:
                System.out.println("Invalid role");
                return null;
        }
    }
}
