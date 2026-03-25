package authentication.factory;

import authentication.model.UserRole;
import authentication.strategy.*;
import config.ServiceConfig;
import otp.service.OtpService;

import java.util.Scanner;

public class AuthStrategyFactory {
    private final Scanner input;
    private final ServiceConfig serviceConfig;
    private final OtpService optService;

    public AuthStrategyFactory(Scanner input, ServiceConfig serviceConfig, OtpService optService) {
        this.input = input;
        this.serviceConfig = serviceConfig;
        this.optService = optService;
    }

    public AuthLoginStretegy getLoginStrategy(UserRole role) {
        switch (role) {
            case ADMIN:
                return new AdminAuthStretegy(input, serviceConfig.getAdminService(), optService);
            case OWNER:
                return new OwnerAuthStrategy(input, serviceConfig.getVehicleOwnerService(), optService);
            case CUSTOMER:
                return new CustomerAuthStretegy(input, serviceConfig.getCustomerService(), optService);
            default:
                System.out.println("Invalid role");
                return null;
        }
    }

    public AuthRegisterStretegy getRegisterStrategy(UserRole role) {
        switch (role) {
            case OWNER:
                return new OwnerAuthStrategy(input, serviceConfig.getVehicleOwnerService(), optService);
            case CUSTOMER:
                return new CustomerAuthStretegy(input, serviceConfig.getCustomerService(), optService);
            default:
                System.out.println("Invalid role");
                return null;
        }
    }
}