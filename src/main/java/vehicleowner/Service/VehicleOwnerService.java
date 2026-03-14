package vehicleowner.Service;

import vehicleowner.Models.VehicleOwner;
import vehicleowner.Repository.VehicleOwnerRepo;

import java.util.List;

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
