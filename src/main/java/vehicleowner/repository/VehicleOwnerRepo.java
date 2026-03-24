package vehicleowner.repository;

import vehicleowner.models.VehicleOwner;

import java.util.List;

public interface VehicleOwnerRepo {
    boolean save(VehicleOwner owner);

    boolean update(VehicleOwner owner);

    boolean deactivateById(String id);

    boolean activateById(String id);

    List<VehicleOwner> findAll();

    VehicleOwner findById(String id);

    VehicleOwner findByEmail(String email);
}