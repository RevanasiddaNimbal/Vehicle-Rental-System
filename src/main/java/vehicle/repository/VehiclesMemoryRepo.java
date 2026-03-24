package vehicle.repository;

import vehicle.models.Status;
import vehicle.models.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehiclesMemoryRepo implements VehicleRepo {
    private final Map<String, Vehicle> storage = new HashMap<>();

    @Override
    public boolean save(Vehicle vehicle) {
        if (vehicle == null || vehicle.getId() == null) return false;
        storage.put(vehicle.getId(), vehicle);
        return true;
    }

    @Override
    public boolean update(Vehicle vehicle) {
        if (vehicle == null || vehicle.getId() == null) return false;
        if (!storage.containsKey(vehicle.getId())) return false;

        storage.put(vehicle.getId(), vehicle);
        return true;
    }

    @Override
    public boolean deleteById(String id) {
        if (id == null) return false;
        return storage.remove(id) != null;
    }

    @Override
    public List<Vehicle> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Vehicle> findByStatus(Status status) {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle vehicle : storage.values()) {
            if (vehicle.getStatus() == status) {
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }

    @Override
    public Vehicle findById(String id) {
        if (id == null) return null;
        return storage.get(id);
    }

    @Override
    public List<Vehicle> findByOwnerId(String ownerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle vehicle : storage.values()) {
            if (ownerId != null && ownerId.equals(vehicle.getOwnerId())) {
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }
}