package penalty.repository;

import penalty.model.Penalty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PenaltyMemoryRepo implements PenaltyRepo {
    Map<String, Penalty> storage = new HashMap<>();

    @Override
    public boolean save(Penalty penalty) {
        if (penalty == null) return false;
        storage.put(penalty.getId(), penalty);
        return true;
    }

    @Override
    public List<Penalty> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Penalty> findByVehicleId(String vehicleId) {
        List<Penalty> penalties = new ArrayList<>();
        for (Penalty penalty : storage.values()) {
            if (penalty.getVehicleId().equals(vehicleId)) {
                penalties.add(penalty);
            }
        }
        return penalties;
    }

    @Override
    public List<Penalty> findByCustomerId(String customerId) {
        List<Penalty> penalties = new ArrayList<>();
        for (Penalty penalty : storage.values()) {
            if (penalty.getCustomerId().equals(customerId)) {
                penalties.add(penalty);
            }
        }
        return penalties;
    }
}
