package authentication.factory;

import authentication.strategy.*;
import config.ServiceConfig;
import user.model.UserRole;

import java.util.Scanner;

public class AuthStrategyFactory {
    private final Scanner input;
    private final ServiceConfig serviceConfig;

    public AuthStrategyFactory(Scanner input, ServiceConfig serviceConfig) {
        this.input = input;
        this.serviceConfig = serviceConfig;
    }

    public AuthLoginStretegy getLoginStrategy(UserRole role) {
        switch (role) {
            case ADMIN:
                return new AdminAuthStretegy(input, serviceConfig.getAdminService(), serviceConfig.getOtpService());
            case OWNER:
                return new OwnerAuthStrategy(input, serviceConfig.getVehicleOwnerService(), serviceConfig.getOtpService());
            case CUSTOMER:
                return new CustomerAuthStretegy(input, serviceConfig.getCustomerService(), serviceConfig.getOtpService());
            default:
                System.out.println("Invalid role");
                return null;
        }
    }

    public AuthRegisterStretegy getRegisterStrategy(UserRole role) {
        switch (role) {
            case OWNER:
                return new OwnerAuthStrategy(input, serviceConfig.getVehicleOwnerService(), serviceConfig.getOtpService());
            case CUSTOMER:
                return new CustomerAuthStretegy(input, serviceConfig.getCustomerService(), serviceConfig.getOtpService());
            default:
                System.out.println("Invalid role");
                return null;
        }
    }
}