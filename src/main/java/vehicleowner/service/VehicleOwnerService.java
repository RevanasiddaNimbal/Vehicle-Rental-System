package vehicleowner.service;

import exception.DuplicateResourceException;
import exception.InactiveUserException;
import exception.ResourceNotFoundException;
import util.PasswordUtil;
import vehicleowner.models.VehicleOwner;
import vehicleowner.repository.VehicleOwnerRepo;

import java.util.List;

public class VehicleOwnerService {
    private final VehicleOwnerRepo repository;

    public VehicleOwnerService(VehicleOwnerRepo repository) {
        this.repository = repository;
    }

    public boolean addVehicleOwner(VehicleOwner vehicleOwner) {
        if (repository.findByEmail(vehicleOwner.getEmail()) != null) {
            throw new DuplicateResourceException("Owner with this email already exists.");
        }
        return repository.save(vehicleOwner);
    }

    public boolean updateVehicleOwner(VehicleOwner vehicleOwner) {
        VehicleOwner existing = repository.findById(vehicleOwner.getId());
        if (existing == null) {
            throw new ResourceNotFoundException("Vehicle Owner ID not found.");
        }
        if (!existing.isActive()) {
            throw new InactiveUserException("User is no longer in the system. Please contact support.");
        }
        return repository.update(vehicleOwner);
    }

    public boolean resetPassword(String ownerId, String oldPassword, String newPassword) {
        VehicleOwner owner = getVehicleOwnerById(ownerId);

        if (!PasswordUtil.verify(oldPassword, owner.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password.");
        }

        owner.setPassword(PasswordUtil.getHashPassword(newPassword));

        return repository.update(owner);
    }

    public boolean deactivateOwnerById(String id) {
        if (!repository.deactivateById(id)) {
            throw new ResourceNotFoundException("Failed to block: Vehicle Owner ID not found.");
        }
        return true;
    }

    public boolean activateOwnerById(String id) {
        if (!repository.activateById(id)) {
            throw new ResourceNotFoundException("Failed to activate: Vehicle Owner ID not found.");
        }
        return true;
    }

    public List<VehicleOwner> getVehicleOwners() {
        return repository.findAll();
    }

    public VehicleOwner getVehicleOwnerById(String id) {
        VehicleOwner owner = repository.findById(id);
        if (owner == null) {
            throw new ResourceNotFoundException("Vehicle Owner not found with ID: " + id);
        }
        if (!owner.isActive()) {
            throw new InactiveUserException("User is no longer in the system. Please contact support.");
        }
        return owner;
    }

    public VehicleOwner getVehicleOwnerByEmail(String email) {
        VehicleOwner owner = repository.findByEmail(email);
        if (owner != null && !owner.isActive()) {
            throw new InactiveUserException("User is no longer in the system. Please contact support.");
        }
        return owner;
    }
}