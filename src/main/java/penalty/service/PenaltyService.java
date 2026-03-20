package penalty.service;

import penalty.factory.PenaltyStrategyFactory;
import penalty.model.Penalty;
import penalty.model.PenaltyReason;
import penalty.model.PenaltyType;
import penalty.repository.PenaltyRepo;
import penalty.stretegy.PenaltyStrategy;
import rental.model.Rental;
import util.IdGeneratorUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PenaltyService {
    private final PenaltyRepo penaltyRepo;
    private final PenaltyStrategyFactory penaltyStrategyFactory;

    public PenaltyService(PenaltyRepo penaltyRepo, PenaltyStrategyFactory penaltyStrategyFactory) {
        this.penaltyRepo = penaltyRepo;
        this.penaltyStrategyFactory = penaltyStrategyFactory;
    }

    public Penalty getLeteReturnPenalty(Rental rental, PenaltyType penaltyType) {
        PenaltyStrategy strategy = penaltyStrategyFactory.getPenaltyStrategy(penaltyType);
        if (strategy == null) return null;
        Penalty penalty = new Penalty(IdGeneratorUtil.generatePenaltyId(), rental.getId(), rental.getVehicleId(), rental.getCustomerId(), 0, PenaltyType.LATE_RETURN, PenaltyReason.VEHICLE_RETURNED_LATE, LocalDate.now());
        strategy.calculate(rental, penalty);
        return penalty;
    }

    public List<Penalty> calculatePenalties(List<Rental> rentals, PenaltyType penaltyType) {
        List<Penalty> penalties = new ArrayList<>();
        if (rentals == null || rentals.isEmpty()) return penalties;

        for (Rental rental : rentals) {
            Penalty penalty = getLeteReturnPenalty(rental, penaltyType);
            if (penalty != null && penalty.getAmount() > 0) {
                penalties.add(penalty);
                penaltyRepo.save(penalty);
            }
        }
        return penalties;
    }

    public List<Penalty> getPenaltiesByCustomerId(String customerId) {
        if (customerId == null) return null;
        return penaltyRepo.findByCustomerId(customerId);
    }

    public List<Penalty> getAllPenalty() {
        return penaltyRepo.findAll();
    }
}
