package vehicleowner.repository;

import vehicleowner.models.VehicleOwner;

import java.util.List;

public interface VehicleOwnerRepo {
    boolean save(VehicleOwner Owner);

    boolean update(VehicleOwner Owner);

    boolean deactivateById(String id);

    boolean activateById(String id);

    List<VehicleOwner> findAll();

    VehicleOwner findById(String id);

    VehicleOwner findByEmail(String email);
}
