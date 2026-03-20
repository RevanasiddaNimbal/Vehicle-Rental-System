package penalty.repository;

import penalty.model.Penalty;

import java.util.List;

public interface PenaltyRepo {
    boolean save(Penalty penalty);

    List<Penalty> findAll();

    List<Penalty> findByVehicleId(String vehicleId);

    List<Penalty> findByCustomerId(String customerId);
}
