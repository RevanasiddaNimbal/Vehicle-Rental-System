package authentication.strategy;

import authentication.model.AuthUser;
import exception.DuplicateResourceException;
import exception.InactiveUserException;
import exception.ResourceNotFoundException;
import otp.service.OtpService;
import util.*;
import vehicleowner.models.VehicleOwner;
import vehicleowner.service.VehicleOwnerService;

import java.util.Scanner;

public class OwnerAuthStrategy implements AuthLoginStretegy, AuthRegisterStrategy, PasswordRecoveryStrategy {
    private final VehicleOwnerService service;
    private final OtpService otpService;
    private final Scanner input;

    public OwnerAuthStrategy(Scanner input, VehicleOwnerService service, OtpService otpService) {
        this.input = input;
        this.service = service;
        this.otpService = otpService;
    }

    @Override
    public AuthUser register() {
        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        try {
            if (service.getVehicleOwnerByEmail(email) != null) {
                System.out.println("Vehicle Owner Already Exists");
                return null;
            }
        } catch (InactiveUserException e) {
            System.out.println("Registration Failed: " + e.getMessage());
            return null;
        }

        String name = InputUtil.readString(input, "Enter Full Name");
        String phone = InputUtil.readValidPhone(input, "Enter Phone Number");
        String address = InputUtil.readString(input, "Enter Address");
        String password = InputUtil.readValidPassword(input, "Enter Password");

        if (!OtpUtil.isVerifiedOtp(input, otpService, email)) {
            System.out.println("Registration Failed: OTP Verification was unsuccessful.");
            return null;
        }

        String id = IdGeneratorUtil.generate(IdPrefix.OWN);

        VehicleOwner owner = new VehicleOwner(id, name, email, phone, PasswordUtil.getHashPassword(password), address);

        try {
            if (service.addVehicleOwner(owner)) {
                System.out.println("Vehicle Owner Registered Successfully");
                return owner;
            } else {
                System.out.println("Failed to Register Vehicle Owner");
                return null;
            }
        } catch (DuplicateResourceException e) {
            System.out.println("Registration Failed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public AuthUser login() {
        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        String password = InputUtil.readString(input, "Enter Password");

        try {
            VehicleOwner owner = service.getVehicleOwnerByEmail(email);

            if (owner != null && PasswordUtil.verify(password, owner.getPassword())) {
                System.out.println("Login Successful.");
                return owner;
            } else if (owner == null) {
                System.out.println("Vehicle owner not found. Please register.");
                return null;
            } else {
                System.out.println("Invalid email or password.");
                return null;
            }
        } catch (InactiveUserException e) {
            System.out.println("Login Failed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void forgotPassword() {
        String email = InputUtil.readValidEmail(input, "Enter Email Address");

        try {
            VehicleOwner owner = service.getVehicleOwnerByEmail(email);

            if (owner == null) {
                System.out.println("Vehicle owner not found. Please register.");
                return;
            }

            String password = InputUtil.readValidPassword(input, "Enter new Password");

            if (OtpUtil.isVerifiedOtp(input, otpService, email)) {
                owner.setPassword(PasswordUtil.getHashPassword(password));

                if (service.updateVehicleOwner(owner)) {
                    System.out.println("Password Reset Successfully. Please login.");
                } else {
                    System.out.println("Failed to Reset Password. Please try again.");
                }
            } else {
                System.out.println("Failed to verify OTP. Please try again.");
            }

        } catch (InactiveUserException | ResourceNotFoundException e) {
            System.out.println("Failed to reset password: " + e.getMessage());
        }
    }
}