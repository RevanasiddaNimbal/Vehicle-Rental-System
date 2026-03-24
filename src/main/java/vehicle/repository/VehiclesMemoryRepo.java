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
        Vehicle existing = storage.get(vehicle.getId());
        if (existing == null || !existing.isActive()) return false;

        storage.put(vehicle.getId(), vehicle);
        return true;
    }

    @Override
    public boolean deleteById(String id) {
        if (id == null) return false;

        Vehicle existing = storage.get(id);
        if (existing == null || !existing.isActive()) return false;

        existing.setActive(false);
        storage.put(id, existing);
        return true;
    }


    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : storage.values()) {
            if (v.isActive()) result.add(v);
        }
        return result;
    }

    @Override
    public List<Vehicle> findByStatus(Status status) {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle vehicle : storage.values()) {
            if (vehicle.isActive() && vehicle.getStatus() == status) {
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }

    @Override
    public Vehicle findById(String id) {
        if (id == null) return null;

        Vehicle v = storage.get(id);
        return (v != null && v.isActive()) ? v : null;
    }

    @Override
    public List<Vehicle> findByOwnerId(String ownerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle vehicle : storage.values()) {
            if (vehicle.isActive() && ownerId != null && ownerId.equals(vehicle.getOwnerId())) {
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }
}