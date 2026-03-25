package authentication.factory;

import admin.service.AdminService;
import authentication.model.UserRole;
import authentication.strategy.*;
import customer.service.CustomerService;
import otp.service.OtpService;
import vehicleowner.service.VehicleOwnerService;

import java.util.Scanner;

public class AuthStrategyFactory {
    private final VehicleOwnerService ownerService;
    private final AdminService adminService;
    private final OtpService optService;
    private final CustomerService customerService;
    private final Scanner input;

    public AuthStrategyFactory(Scanner input, VehicleOwnerService ownerService, AdminService adminService, CustomerService customerService, OtpService optService) {
        this.input = input;
        this.ownerService = ownerService;
        this.adminService = adminService;
        this.optService = optService;
        this.customerService = customerService;
    }

    public AuthLoginStretegy getLoginStrategy(UserRole role) {
        switch (role) {
            case ADMIN:
                return new AdminAuthStretegy(input, adminService, optService);
            case OWNER:
                return new OwnerAuthStrategy(input, ownerService, optService);
            case CUSTOMER:
                return new CustomerAuthStretegy(input, customerService, optService);
            default:
                System.out.println("Invalid role");
                return null;
        }
    }

    public AuthRegisterStretegy getRegisterStrategy(UserRole role) {
        switch (role) {
            case OWNER:
                return new OwnerAuthStrategy(input, ownerService, optService);
            case CUSTOMER:
                return new CustomerAuthStretegy(input, customerService, optService);
            default:
                System.out.println("Invalid role");
                return null;
        }
    }
}
