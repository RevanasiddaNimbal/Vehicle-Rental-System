package vehicleowner.controller;

import UI.UserPrinter;
import exception.DuplicateResourceException;
import exception.InactiveUserException;
import exception.ResourceNotFoundException;
import util.InputUtil;
import vehicleowner.models.VehicleOwner;
import vehicleowner.service.VehicleOwnerService;

import java.util.List;
import java.util.Scanner;

public class VehicleOwnerController {
    private final VehicleOwnerService service;
    private final UserPrinter<VehicleOwner> printer;

    public VehicleOwnerController(VehicleOwnerService service, UserPrinter<VehicleOwner> printer) {
        this.service = service;
        this.printer = printer;
    }

    public void deactivateVehicleOwner(Scanner input) {
        String id = InputUtil.readString(input, "Enter Vehicle Owner ID");
        try {
            service.deactivateOwnerById(id);
            System.out.println("Blocked Vehicle Owner Successfully.");
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void activateVehicleOwner(Scanner input) {
        String id = InputUtil.readString(input, "Enter Vehicle Owner ID");
        try {
            service.activateOwnerById(id);
            System.out.println("Activated Vehicle Owner Successfully.");
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateVehicleOwner(Scanner input, String ownerId) {
        try {
            VehicleOwner vehicleOwner = service.getVehicleOwnerById(ownerId);

            boolean updating = true;
            boolean hasChanges = false;

            while (updating) {
                System.out.println("\n---- Update Vehicle Owner ----");
                System.out.println("1. Name");
                System.out.println("2. Email address");
                System.out.println("3. Phone number");
                System.out.println("4. Address");
                System.out.println("5. Password");
                System.out.println("0. Save and Exit");

                int choice = InputUtil.readPositiveInt(input, "Enter choice");

                switch (choice) {
                    case 1:
                        vehicleOwner.setName(InputUtil.readString(input, "Enter new full name"));
                        hasChanges = true;
                        break;
                    case 2:
                        vehicleOwner.setEmail(InputUtil.readValidEmail(input, "Enter new email address"));
                        hasChanges = true;
                        break;
                    case 3:
                        vehicleOwner.setPhone(InputUtil.readValidPhone(input, "Enter new phone number"));
                        hasChanges = true;
                        break;
                    case 4:
                        vehicleOwner.setAddress(InputUtil.readString(input, "Enter new address"));
                        hasChanges = true;
                        break;
                    case 5:
                        vehicleOwner.setPassword(InputUtil.readValidPassword(input, "Enter new password"));
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
                service.updateVehicleOwner(vehicleOwner);
                System.out.println("Vehicle Owner Updated Successfully.");
            } else {
                System.out.println("No changes were made.");
            }

        } catch (InactiveUserException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ResourceNotFoundException | DuplicateResourceException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void resetPassword(Scanner input, String ownerId) {
        String oldPassword = InputUtil.readString(input, "Enter your CURRENT password");
        String newPassword = InputUtil.readValidPassword(input, "Enter your NEW password");

        try {
            if (service.resetPassword(ownerId, oldPassword, newPassword)) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("Failed to update password. Please try again later.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InactiveUserException e) {
            System.out.println("Error: User account is no longer active.");
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: Vehicle Owner not found.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void viewOwners() {
        List<VehicleOwner> owners = service.getVehicleOwners();
        if (owners.isEmpty()) {
            System.out.println("There are no owners available.");
            return;
        }
        printer.print(owners);
    }
}