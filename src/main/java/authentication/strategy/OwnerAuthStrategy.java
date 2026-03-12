package authentication.strategy;

import authentication.model.AuthUser;
import util.IdGenerator;
import util.InputUtil;
import util.PasswordUtil;
import vehicleowner.Models.VehicleOwner;
import vehicleowner.Service.VehicleOwnerService;

import java.util.Scanner;

public class OwnerAuthStrategy implements AuthStrategy {
    private final VehicleOwnerService service;
    private final Scanner input;

    public OwnerAuthStrategy(Scanner input, VehicleOwnerService service) {
        this.input = input;
        this.service = service;
    }

    @Override
    public AuthUser register() {
        String id = IdGenerator.generateVehicleOwnerId();
        String name = InputUtil.readString(input, "Enter Full Name");
        String email = InputUtil.readString(input, "Enter Email Address");
        String phone = InputUtil.readString(input, "Enter Phone Number");
        String Address = InputUtil.readString(input, "Enter Address");
        String password = InputUtil.readString(input, "Enter Password");
        if (service.getVehicleOwnerByEmail(email) != null) {
            System.out.println("Vehicle Owner Already Exists");
            return null;
        }
        VehicleOwner owner = new VehicleOwner(id, name, email, phone, PasswordUtil.getHashPassword(password), Address);
        if (service.addVehicleOwner(owner)) {
            System.out.println("Vehicle Owner Registered Successfully");
            return owner;
        } else {
            System.out.println("Failed Register Vehicle Owner");
            return null;
        }
    }

    @Override
    public AuthUser login() {
        String email = InputUtil.readString(input, "Enter Email Address");
        String password = InputUtil.readString(input, "Enter Password");
        VehicleOwner owner = service.getVehicleOwnerByEmail(email);
        if (owner != null &&
                PasswordUtil.verify(password, owner.getPassword())) {

            System.out.println("Login Successful.");
            return owner;
        } else {
            System.out.println("Invalid credentials.");
            return null;
        }
    }
}
