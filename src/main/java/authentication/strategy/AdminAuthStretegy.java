package authentication.strategy;

import admin.model.Admin;
import admin.service.AdminService;
import authentication.model.AuthUser;
import util.InputUtil;

import java.util.Scanner;

public class AdminAuthStretegy implements AuthLoginStretegy {
    private final Scanner input;
    private final AdminService service;

    public AdminAuthStretegy(Scanner input, AdminService service) {
        this.input = input;
        this.service = service;
    }

    @Override
    public AuthUser login() {
        String email = InputUtil.readString(input, "Enter Email Address");
        String password = InputUtil.readString(input, "Enter Password");
        Admin admin = service.getAdminByEmail(email);
        if (admin != null && admin.getPassword().equals(password)) {
            System.out.println("Login Successful.");
            return admin;
        } else {
            System.out.println("Invalid credentials.");
            return null;
        }

    }
}
