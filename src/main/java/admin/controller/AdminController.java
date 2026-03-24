package admin.controller;

import admin.model.Admin;
import admin.service.AdminService;
import exception.DuplicateResourceException;
import exception.ResourceNotFoundException;
import util.InputUtil;

import java.util.Scanner;

public class AdminController {
    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    public void updateAdmin(Scanner input, String id) {
        try {
            Admin admin = service.getAdminById(id);
            boolean updating = true;
            boolean hasChanges = false;

            while (updating) {
                System.out.println("\n---- Update Admin -----");
                System.out.println("1. Username");
                System.out.println("2. Email");
                System.out.println("0. Save and go Back");

                int choice = InputUtil.readPositiveInt(input, "Enter your choice");

                switch (choice) {
                    case 1:
                        admin.setUsername(InputUtil.readString(input, "Enter your new Username"));
                        hasChanges = true;
                        break;
                    case 2:
                        admin.setEmail(InputUtil.readValidEmail(input, "Enter your new Email"));
                        hasChanges = true;
                        break;
                    case 0:
                        updating = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }

            if (hasChanges) {
                service.updateAdmin(admin);
                System.out.println("Admin updated successfully.");
            } else {
                System.out.println("No changes were made.");
            }

        } catch (ResourceNotFoundException | DuplicateResourceException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void resetPassword(Scanner input, String adminId) {
        try {
            if (service.resetPassword(input, adminId)) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("Failed to update password. Please try again later.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: Admin not found.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}