package cancellation.repository;

import cancellation.model.CancellationRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CancellationMemoryRepo implements CancellationRepo {
    Map<String, CancellationRecord> storage = new HashMap<>();

    @Override
    public boolean save(CancellationRecord record) {
        if (record == null) return false;
        storage.put(record.getId(), record);
        return true;
    }

    @Override
    public List<CancellationRecord> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<CancellationRecord> findByCustomerId(String customerId) {
        List<CancellationRecord> result = new ArrayList<>();
        for (CancellationRecord record : storage.values()) {
            if (record.getCustomerId().equals(customerId)) {
                result.add(record);
            }
        }
        return result;
    }

    @Override
    public List<CancellationRecord> findByOwnerId(String ownerId) {
        List<CancellationRecord> result = new ArrayList<>();
        for (CancellationRecord record : storage.values()) {
            if (record.getOwnerId().equals(ownerId)) {
                result.add(record);
            }
        }
        return result;
    }
}
