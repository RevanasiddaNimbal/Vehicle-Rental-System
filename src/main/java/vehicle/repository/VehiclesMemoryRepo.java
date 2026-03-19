package vehicle.repository;

import vehicle.models.Status;
import vehicle.models.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehiclesMemoryRepo implements VehicleRepo {
    Map<String, Vehicle> Storage = new HashMap<String, Vehicle>();

    @Override
    public boolean save(Vehicle vehicle) {
        Storage.put(vehicle.getId(), vehicle);
        return true;
    }

    @Override
    public boolean update(Vehicle vehicle) {
        Storage.put(vehicle.getId(), vehicle);
        return true;
    }

    @Override
    public boolean deleteById(String id) {
        Storage.remove(id);
        return true;
    }

    @Override
    public List<Vehicle> findAll() {
        return new ArrayList<Vehicle>(Storage.values());
    }

    public List<Vehicle> findByStatus(Status status) {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle vehicle : Storage.values()) {
            if (vehicle.getStatus().equals(status)) {
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }

    @Override
    public Vehicle findById(String id) {
        return Storage.get(id);
    }

    @Override
    public List<Vehicle> findByOwnerId(String ownerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle vehicle : Storage.values()) {
            if (vehicle.getOwnerId().equals(ownerId)) {
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }

}
