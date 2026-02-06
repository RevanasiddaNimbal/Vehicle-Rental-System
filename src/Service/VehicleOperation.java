package Service;

import Models.Vehicle;

import java.util.List;

public interface VehicleOperation {
    boolean addVehicle(Vehicle vehicle);

    boolean updateVehicle(Vehicle vehicle);

    boolean deleteVehicle(Vehicle vehicle);

    List<Vehicle> getVehicles();

    Vehicle findById(String id);

}
