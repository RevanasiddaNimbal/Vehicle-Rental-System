package vehicle.Repository;

import vehicle.Models.Vehicle;

import java.util.List;

public interface VehicleRepo {
    boolean save(Vehicle vehicle);

    boolean update(Vehicle vehicle);

    boolean deleteById(String id);

    List<Vehicle> findAll();

    Vehicle findById(String id);

}
