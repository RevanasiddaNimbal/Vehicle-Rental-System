package vehicle.service;

import vehicle.models.Status;
import vehicle.models.Vehicle;
import vehicle.repository.VehicleRepo;

import java.util.List;

public class VehicleService {
    private final VehicleRepo repository;

    public VehicleService(VehicleRepo repository) {
        this.repository = repository;
    }

    public boolean addVehicle(Vehicle vehicle) {
        if (repository.save(vehicle))
            return true;
        else
            return false;
    }

    public boolean updateStatusById(String id, Status status) {
        Vehicle vehicle = repository.findById(id);
        if (vehicle == null) {
            return false;
        }
        vehicle.setStatus(status);
        return repository.update(vehicle);
    }

    public boolean updateVehicle(Vehicle vehicle) {
        if (repository.findById(vehicle.getId()) == null) {
            return false;
        }
        return repository.update(vehicle);
    }

    public List<Vehicle> getVehiclesByStatus(Status status) {
        return repository.findByStatus(status);
    }

    public boolean deleteVehicle(Vehicle vehicle) {
        if (repository.findById(vehicle.getId()) == null) {
            return false;
        }
        return repository.deleteById(vehicle.getId());
    }

    public List<Vehicle> getVehicles() {
        return repository.findAll();
    }

    public Vehicle getVehiclesById(String id) {
        return repository.findById(id);

    }

    public List<Vehicle> getVehiclesByOwnerId(String ownerId) {
        return repository.findByOwnerId(ownerId);
    }

}
