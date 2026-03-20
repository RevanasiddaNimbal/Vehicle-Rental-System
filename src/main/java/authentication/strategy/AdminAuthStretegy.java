package authentication.strategy;

import admin.model.Admin;
import admin.service.AdminService;
import authentication.model.AuthUser;
import authentication.service.OtpService;
import util.InputUtil;
import util.OtpUtil;

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
            System.out.println("Admin not found.Please contact rental support.");
            return null;
        }
        
        if (admin.getPassword().equals(password)) {
            System.out.println("Login Successful.");
            return admin;
        } else {
            System.out.println("Invalid credentials.");
            return null;
        }

    }

    @Override
    public void resetPassword() {
        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        Admin admin = adminService.getAdminByEmail(email);

        if (admin == null) {
            System.out.println("Admin Not Found.Please register.");
            return;
        }

        String password = InputUtil.readValidPassword(input, "Enter new Password");

        if (OtpUtil.isVerifiedOtp(input, otpService, email)) {
            admin.setPassword(password);
            if (adminService.updateAdmin(admin)) {
                System.out.println("Password reset successful.Please Login.");
            } else {
                System.out.println("Password reset failed.Please Try Again.");
            }
        } else {
            System.out.println("Failed to reset Password. Please try again.");
        }

    }
}
