package vehicle.Repository;

import vehicle.Models.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryVehicleRepo implements VehicleRepo {
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

    @Override
    public Vehicle findById(String id) {
        return Storage.get(id);
    }

}
