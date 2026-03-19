package vehicleowner.controller;

import UI.UserPrinter;
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
        if (service.getVehicleOwnerById(id) == null) {
            System.out.println("Vehicle Owner ID Not Found");
            return;
        }

        if (service.deactivateOwnerById(id)) {
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
        if (service.activateOwnerById(id)) {
            System.out.println("activated Vehicle Owner Successfully.");
        } else {
            System.out.println("Failed to activate vehicle Owner.");
        }
    }

    public void updateVehicleOwner(Scanner input, String ownerId) {
        VehicleOwner vehicleOwner = service.getVehicleOwnerById(ownerId);
        if (vehicleOwner == null) {
            System.out.println("Vehicle Owner ID Not Found");
            return;
        }

        System.out.println("----Update Vehicle Owner----");
        System.out.println("1. Name");
        System.out.println("2. Email address");
        System.out.println("3. Phone number");
        System.out.println("4. Address");
        System.out.println("5. Password");
        System.out.println("0. back");
        int choice = InputUtil.readPositiveInt(input, "Enter choice");
        switch (choice) {
            case 1:
                String name = InputUtil.readString(input, "Enter new full name");
                vehicleOwner.setName(name);
                break;
            case 2:
                String email = InputUtil.readValidEmail(input, "Enter new email address");
                vehicleOwner.setEmail(email);
                break;
            case 3:
                String phone = InputUtil.readValidPhone(input, "Enter new phone number");
                vehicleOwner.setPhone(phone);
                break;
            case 4:
                String Address = InputUtil.readString(input, "Enter new address");
                vehicleOwner.setAddress(Address);
                break;
            case 5:
                String password = InputUtil.readValidPassword(input, "Enter new password");
                vehicleOwner.setPassword(password);
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
        List<VehicleOwner> owners = service.getVehicleOwners();
        if (owners.isEmpty()) {
            System.out.println("There are no owners available.");
            return;
        }
        printer.print(owners);
    }
}
