package UI;

import admin.controller.AdminController;
import util.InputUtil;

import java.util.Scanner;

public class AdminAccountMenu implements UsersMenu {
    private final AdminController adminController;
    private final Scanner input;

    public AdminAccountMenu(Scanner input, AdminController adminController) {
        this.adminController = adminController;
        this.input = input;
    }

    @Override
    public void show(String adminId) {
        int choice;
        while (true) {
            try {
                System.out.println("\n========= Account Management Menu =========");
                System.out.println("1. Reset Password");
                System.out.println("1. Update My account");
                System.out.println("0. back");
                choice = InputUtil.readPositiveInt(input, "Enter your choice");
                switch (choice) {
                    case 1:
                        adminController.resetPassword(input, adminId);
                        break;
                    case 2:
                        adminController.updateAdmin(input, adminId);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            }
        }
    }
}
