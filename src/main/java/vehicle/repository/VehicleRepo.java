package vehicle.repository;

import vehicle.models.Status;
import vehicle.models.Vehicle;

import java.util.List;

public interface VehicleRepo {
    boolean save(Vehicle vehicle);

    boolean update(Vehicle vehicle);

    boolean deleteById(String id);

    List<Vehicle> findByStatus(Status status);

    List<Vehicle> findAll();

    Vehicle findById(String id);

    List<Vehicle> findByOwnerId(String ownerId);

}
