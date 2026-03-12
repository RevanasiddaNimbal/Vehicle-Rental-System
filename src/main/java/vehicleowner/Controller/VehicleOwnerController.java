package vehicleowner.Controller;

import util.InputUtil;
import vehicleowner.Models.VehicleOwner;
import vehicleowner.Service.VehicleOwnerService;

import java.util.Scanner;

public class VehicleOwnerController {
    private final VehicleOwnerService service;

    public VehicleOwnerController(VehicleOwnerService service) {
        this.service = service;
    }

    public void deactivateVehicleOwner(Scanner input) {
        String id = InputUtil.readString(input, "Enter Vehicle Owner ID");
        if (service.getVehicleOwnerById(id) == null) {
            System.out.println("Vehicle Owner ID Not Found");
            return;
        }

        if (service.deactivateVehicleOwner(id)) {
            System.out.println("Blocked Vehicle Owner Successfully.");
        } else {
            System.out.println("Failed to block vehicle Owner.");
        }
    }

    public void activateVehicleOwner(Scanner input) {
        String id = InputUtil.readString(input, "Enter Vehicle Owner ID");
        if (service.getVehicleOwnerById(id) == null) {
            System.out.println("Vehicle Owner ID Not Found");
            return;
        }
        if (service.activateVehicleOwner(id)) {
            System.out.println("activated Vehicle Owner Successfully.");
        } else {
            System.out.println("Failed to activate vehicle Owner.");
        }
    }

    public void updateVehicleOwner(Scanner input) {
        String id = InputUtil.readString(input, "Enter Vehicle Owner ID");
        VehicleOwner vehicleOwner = service.getVehicleOwnerById(id);
        if (vehicleOwner == null) {
            System.out.println("Vehicle Owner ID Not Found");
            return;
        }

        System.out.println("----Update Vehicle Owner----");
        System.out.println("1. Name");
        System.out.println("2. Email address");
        System.out.println("3. Phone number");
        System.out.println("4. Address");
        System.out.println("0. back");
        int choice = InputUtil.readPositiveInt(input, "Enter choice");
        switch (choice) {
            case 1:
                String name = InputUtil.readString(input, "Enter new full name");
                vehicleOwner.setName(name);
                break;
            case 2:
                String email = InputUtil.readString(input, "Enter new email address");
                vehicleOwner.setEmail(email);
                break;
            case 3:
                String phone = InputUtil.readString(input, "Enter new phone number");
                vehicleOwner.setPhone(phone);
                break;
            case 4:
                String Address = InputUtil.readString(input, "Enter new address");
                vehicleOwner.setAddress(Address);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice");
                return;
        }
        if (service.updateVehicleOwner(vehicleOwner)) {
            System.out.println("Vehicle Owner Updated Successfully");
        } else {
            System.out.println("Failed to update vehicle Owner.");
        }

    }

    public void viewOwners() {
        if (service.getVehicleOwners().isEmpty()) {
            System.out.println("There are no owners available.");
            return;
        }

        System.out.println("---------------------------------------------------------------");
        System.out.printf(
                "%-12s %-15s %-25s %-15s %-20s %-8s%n",
                "ID", "Name", "Email", "Phone", "Address", "Active"
        );
        System.out.println("---------------------------------------------------------------");

        for (VehicleOwner owner : service.getVehicleOwners()) {
            System.out.printf(
                    "%-12s %-15s %-25s %-15s %-20s %-8s%n",
                    owner.getId(),
                    owner.getName(),
                    owner.getEmail(),
                    owner.getPhone(),
                    owner.getAddress(),
                    owner.isActive() ? "Yes" : "No"
            );
        }

        System.out.println("---------------------------------------------------------------");
    }

}
