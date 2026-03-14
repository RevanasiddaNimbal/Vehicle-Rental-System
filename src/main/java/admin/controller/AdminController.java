package admin.controller;

import admin.model.Admin;
import admin.service.AdminService;
import util.InputUtil;

import java.util.Scanner;

public class AdminController {
    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    public void updateAdmin(Scanner input, String id) {
        Admin admin = service.getAdminById(id);
        if (admin == null) {
            System.out.println("Admin Not Found");
            return;
        }
        System.out.println("---- Update Admin -----");
        System.out.println("1. Username");
        System.out.println("2. Email");
        System.out.println("3. Password");
        System.out.println("0. back");
        int choice = InputUtil.readPositiveInt(input, "Enter your choice");
        switch (choice) {
            case 1:
                String name = InputUtil.readString(input, "Enter your new  Username");
                admin.setUsername(name);
                break;
            case 2:
                String email = InputUtil.readValidEmail(input, "Enter your new  Email");
                admin.setEmail(email);
                break;
            case 3:
                String password = InputUtil.readValidPassword(input, "Enter your new  Password");
                admin.setPassword(password);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice");
        }
        if (service.updateAdmin(admin)) {
            System.out.println("Admin Updated successfully.");
        } else {
            System.out.println("Failed to Update Admin details.");
        }
    }

}
