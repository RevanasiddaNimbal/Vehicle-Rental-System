package vehicle.Service;

import vehicle.Models.Vehicle;
import vehicle.repository.VehicleRepo;

import java.util.List;

public class VehicleService {
    private final VehicleRepo repository;

    public VehicleService(VehicleRepo repository) {
        this.repository = repository;
    }

    public boolean addVehicle(Vehicle vehicle) {
        if (repository.findById(vehicle.getId()) != null) {
            return false;
        }
        repository.save(vehicle);
        return true;
    }

    public boolean updateVehicle(Vehicle vehicle) {
        if (repository.findById(vehicle.getId()) == null) {
            return false;
        }
        return repository.update(vehicle);
    }

    public boolean deleteVehicle(Vehicle vehicle) {
        if (repository.findById(vehicle.getId()) == null) {
            return false;
        }
        repository.deleteById(vehicle.getId());
        return true;
    }

    public List<Vehicle> getVehicles() {
        return repository.findAll();
    }

    public Vehicle getVehiclesById(String id) {
        return repository.findById(id);

    }
}
