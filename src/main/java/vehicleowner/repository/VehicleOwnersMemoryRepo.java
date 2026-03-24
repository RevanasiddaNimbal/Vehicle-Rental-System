package vehicleowner.repository;

import vehicleowner.models.VehicleOwner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleOwnersMemoryRepo implements VehicleOwnerRepo {
    private final Map<String, VehicleOwner> storage = new HashMap<>();

    @Override
    public boolean save(VehicleOwner owner) {
        if (owner == null || owner.getId() == null) return false;
        storage.put(owner.getId(), owner);
        return true;
    }

    @Override
    public boolean update(VehicleOwner owner) {
        if (owner == null || owner.getId() == null) return false;
        if (!storage.containsKey(owner.getId())) return false;

        VehicleOwner existing = storage.get(owner.getId());
        if (existing == null || !existing.isActive()) return false;

        storage.put(owner.getId(), owner);
        return true;
    }

    @Override
    public boolean deactivateById(String id) {
        if (id == null) return false;

        VehicleOwner existing = storage.get(id);
        if (existing == null || !existing.isActive()) return false;

        existing.deactivate();
        storage.put(id, existing);
        return true;
    }

    @Override
    public boolean activateById(String id) {
        if (id == null) return false;

        VehicleOwner existing = storage.get(id);
        if (existing == null || existing.isActive()) return false;
        
        existing.activate();
        storage.put(id, existing);
        return true;
    }

    @Override
    public List<VehicleOwner> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public VehicleOwner findById(String id) {
        if (id == null) return null;
        return storage.get(id);
    }

    @Override
    public VehicleOwner findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) return null;
        for (VehicleOwner owner : storage.values()) {
            if (owner.isActive() && email.equalsIgnoreCase(owner.getEmail())) {
                return owner;
            }
        }
        return null;
    }
}