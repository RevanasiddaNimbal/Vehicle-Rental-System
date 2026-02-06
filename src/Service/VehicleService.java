package Service;

import Models.Vehicle;

import java.util.ArrayList;

public class VehicleService implements VehicleOperation {
    ArrayList<Vehicle> vehicles = new ArrayList<>();

    @Override
    public Vehicle findById(String id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId().equals(id)) return vehicle;
        }
        return null;
    }

    public boolean addVehicle(Vehicle vehicle) {
        if (findById(vehicle.getId()) != null) {
            return false;
        }
        vehicles.add(vehicle);
        return true;
    }

    @Override
    public boolean updateVehicle(Vehicle vehicle) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId().equals(vehicle.getId())) {
                vehicles.set(i, vehicle);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteVehicle(Vehicle vehicle) {
        if (findById(vehicle.getId()) == null) return false;
        vehicles.remove(vehicle);
        return true;
    }

    @Override
    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }


}
