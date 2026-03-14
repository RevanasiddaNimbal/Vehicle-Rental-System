package vehicleowner.Repository;

import vehicleowner.Models.VehicleOwner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleOwnersMemoryRepo implements VehicleOwnerRepo {
    private final Map<String, VehicleOwner> Storage = new HashMap<>();

    @Override
    public boolean save(VehicleOwner Owner) {
        Storage.put(Owner.getId(), Owner);
        return true;
    }

    @Override
    public boolean update(VehicleOwner Owner) {
        if (Storage.get(Owner.getId()) == null) {
            return false;
        }
        Storage.put(Owner.getId(), Owner);
        return true;
    }

    @Override
    public boolean deactivateById(String id) {
        VehicleOwner owner = Storage.get(id);
        if (owner != null && owner.isActive()) {
            owner.deactivate();
            Storage.put(id, owner);
            return true;
        }
        return false;
    }

    @Override
    public boolean activateById(String id) {
        VehicleOwner owner = Storage.get(id);
        if (owner != null && !owner.isActive()) {
            owner.activate();
            Storage.put(id, owner);
            return true;
        }
        return false;
    }

    @Override
    public VehicleOwner findById(String id) {
        return Storage.get(id);
    }

    @Override
    public List<VehicleOwner> findAll() {
        return new ArrayList<VehicleOwner>(Storage.values());
    }

    @Override
    public VehicleOwner findByEmail(String email) {
        for (VehicleOwner owner : Storage.values()) {
            if (owner.getEmail().equalsIgnoreCase(email)) {
                return owner;
            }
        }

        return null;
    }
}
