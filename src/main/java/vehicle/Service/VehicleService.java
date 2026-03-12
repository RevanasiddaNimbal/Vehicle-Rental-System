package vehicle.Service;

import vehicle.Models.Vehicle;
import vehicle.Repository.VehicleRepo;

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
        return repository.deleteById(vehicle.getId());
    }

    public List<Vehicle> getVehicles() {
        return repository.findAll();
    }

    public Vehicle getVehiclesById(String id) {
        return repository.findById(id);

    }
}
