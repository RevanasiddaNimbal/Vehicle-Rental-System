package vehicleowner.service;

import util.InputUtil;
import vehicleowner.models.VehicleOwner;
import vehicleowner.repository.VehicleOwnerRepo;

import java.util.List;
import java.util.Scanner;

public class VehicleOwnerService {
    private final VehicleOwnerRepo repository;

    public VehicleOwnerService(VehicleOwnerRepo repository) {
        this.repository = repository;
    }

    public boolean addVehicleOwner(VehicleOwner vehicleOwner) {
        if (repository.findByEmail(vehicleOwner.getEmail()) != null) {
            System.out.println("Owner Already Exists.");
            return false;
        }
        return repository.save(vehicleOwner);
    }

    public boolean updateVehicleOwner(VehicleOwner vehicleOwner) {
        if (repository.findById(vehicleOwner.getId()) == null) {
            return false;
        }
        return repository.update(vehicleOwner);
    }

    public boolean resetPassword(Scanner input, String ownerId) {
        VehicleOwner vehicleOwner = repository.findById(ownerId);
        if (vehicleOwner == null) {
            return false;
        }
        String oldPassword = InputUtil.readValidPassword(input, "Enter old password");
        String newPassword = InputUtil.readValidPassword(input, "Enter new password");

        if (!vehicleOwner.getPassword().equals(oldPassword)) {
            System.out.println("Passwords do not match.");
            return false;
        }
        vehicleOwner.setPassword(newPassword);
        return repository.update(vehicleOwner);

    }

    public boolean deactivateOwnerById(String id) {
        if (repository.findById(id) == null) {
            return false;
        }
        return repository.deactivateById(id);
    }

    public boolean activateOwnerById(String id) {
        if (repository.findById(id) == null) {
            return false;
        }
        return repository.activateById(id);
    }

    public List<VehicleOwner> getVehicleOwners() {
        return repository.findAll();
    }

    public VehicleOwner getVehicleOwnerById(String id) {
        return repository.findById(id);
    }

    public VehicleOwner getVehicleOwnerByEmail(String email) {
        return repository.findByEmail(email);
    }
}
