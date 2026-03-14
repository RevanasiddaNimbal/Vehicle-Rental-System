package authentication.strategy;

import authentication.model.AuthUser;
import authentication.service.OtpService;
import util.IdGenerator;
import util.InputUtil;
import util.OtpUtil;
import util.PasswordUtil;
import vehicleowner.Models.VehicleOwner;
import vehicleowner.Service.VehicleOwnerService;

import java.util.Scanner;

public class OwnerAuthStrategy implements AuthLoginStretegy, AuthRegisterStretegy {
    private final VehicleOwnerService service;
    private final OtpService otpService;
    private final Scanner input;

    public OwnerAuthStrategy(Scanner input, VehicleOwnerService service, OtpService otpService) {
        this.input = input;
        this.service = service;
        this.otpService = otpService;
    }

    @Override
    public void register() {
        String id = IdGenerator.generateVehicleOwnerId();
        String name = InputUtil.readString(input, "Enter Full Name");
        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        String phone = InputUtil.readValidPhone(input, "Enter Phone Number");
        String Address = InputUtil.readString(input, "Enter Address");
        String password = InputUtil.readValidPassword(input, "Enter Password");

        if (service.getVehicleOwnerByEmail(email) != null) {
            System.out.println("Vehicle Owner Already Exists");
            return;
        }
        VehicleOwner owner = new VehicleOwner(id, name, email, phone, PasswordUtil.getHashPassword(password), Address, false);

        if (OtpUtil.isVerifiedOtp(input, otpService, email)) {
            owner.activate();
            if (service.addVehicleOwner(owner)) {
                System.out.println("Vehicle Owner Registered Successfully");
            } else {
                System.out.println("Failed to Register Vehicle Owner");
            }
        } else {
            System.out.println("Registration Failed.");
        }
    }


    @Override
    public AuthUser login() {
        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        String password = InputUtil.readString(input, "Enter Password");
        VehicleOwner owner = service.getVehicleOwnerByEmail(email);
        if (owner != null &&
                PasswordUtil.verify(password, owner.getPassword())) {
            if (owner.isActive()) {
                System.out.println("Login Successful.");
                return owner;
            } else {
                System.out.println("This account is no longer active.Please contact vehicle rental support.");
                return null;
            }
        } else if (owner == null) {
            System.out.println("Vehicle owner not found.Please register.");
            return null;
        } else {
            System.out.println("Invalid email and password.");
            return null;
        }
    }

    @Override
    public void resetPassword() {
        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        VehicleOwner owner = service.getVehicleOwnerByEmail(email);
        if (owner == null) {
            System.out.println("Vehicle owner not found.Please register.");
            return;
        }
        String password = InputUtil.readValidPassword(input, "Enter new Password");
        if (OtpUtil.isVerifiedOtp(input, otpService, email)) {
            owner.setPassword(PasswordUtil.getHashPassword(password));
            if (service.updateVehicleOwner(owner)) {
                System.out.println("Password Reset Successfully.Please login");
            } else {
                System.out.println("Failed to Reset Password.Please try again.");
            }
        } else {
            System.out.println("Failed to reset Password.Please try again.");
        }
    }
}
