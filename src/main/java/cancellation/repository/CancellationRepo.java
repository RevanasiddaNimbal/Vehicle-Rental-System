package cancellation.repository;

import cancellation.model.CancellationRecord;

import java.util.List;

public interface CancellationRepo {
    boolean save(CancellationRecord record);

    List<CancellationRecord> findByCustomerId(String customerId);

    List<CancellationRecord> findByOwnerId(String ownerId);

    List<CancellationRecord> findAll();
}
