package authentication.strategy;

import admin.model.Admin;
import admin.service.AdminService;
import authentication.model.AuthUser;
import authentication.service.OtpService;
import util.InputUtil;
import util.OtpUtil;
import util.PasswordUtil;

import java.util.Scanner;

public class AdminAuthStretegy implements AuthLoginStretegy {
    private final Scanner input;
    private final AdminService adminService;
    private final OtpService otpService;

    public AdminAuthStretegy(Scanner input, AdminService adminService, OtpService otpService) {
        this.input = input;
        this.adminService = adminService;
        this.otpService = otpService;
    }

    @Override
    public AuthUser login() {
        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        String password = InputUtil.readString(input, "Enter Password");

        Admin admin = adminService.getAdminByEmail(email);

        if (admin == null) {
            System.out.println("Login Failed: Admin not found. Please contact support.");
            return null;
        }

        if (PasswordUtil.verify(password, admin.getPassword())) {
            System.out.println("Login Successful. Welcome " + admin.getUsername() + "!");
            return admin;
        } else {
            System.out.println("Login Failed: Invalid credentials.");
            return null;
        }
    }

    @Override
    public void forgotPassword() {
        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        Admin admin = adminService.getAdminByEmail(email);

        if (admin == null) {
            System.out.println("Admin Not Found. Please contact support.");
            return;
        }

        String password = InputUtil.readValidPassword(input, "Enter new Password");

        if (OtpUtil.isVerifiedOtp(input, otpService, email)) {
            admin.setPassword(PasswordUtil.getHashPassword(password));

            try {
                if (adminService.updateAdmin(admin)) {
                    System.out.println("Password reset successful. Please Login.");
                } else {
                    System.out.println("Password reset failed. Please Try Again.");
                }
            } catch (Exception e) {
                System.out.println("Error updating admin: " + e.getMessage());
            }
        } else {
            System.out.println("Failed to verify OTP. Please try again.");
        }
    }
}