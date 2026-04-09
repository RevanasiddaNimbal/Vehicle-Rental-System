package penalty.service;

import penalty.factory.PenaltyStrategyFactory;
import penalty.model.Penalty;
import penalty.model.PenaltyReason;
import penalty.model.PenaltyType;
import penalty.repository.PenaltyRepo;
import penalty.strategy.PenaltyStrategy;
import rental.model.Rental;
import util.IdGeneratorUtil;
import util.IdPrefix;

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

    public Penalty getLateReturnPenalty(Rental rental, PenaltyType penaltyType) {
        PenaltyStrategy strategy = penaltyStrategyFactory.getPenaltyStrategy(penaltyType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported penalty type: " + penaltyType);
        }

        double amount = strategy.calculate(rental);

        if (amount <= 0) {
            return null;
        }

        return new Penalty(IdGeneratorUtil.generate(IdPrefix.PEN), rental.getId(), rental.getVehicleId(), rental.getCustomerId(), amount, PenaltyType.LATE_RETURN, PenaltyReason.VEHICLE_RETURNED_LATE, LocalDate.now());
    }

    public List<Penalty> calculatePenalties(List<Rental> rentals, PenaltyType penaltyType) {
        List<Penalty> penalties = new ArrayList<>();
        if (rentals == null || rentals.isEmpty()) return penalties;

        for (Rental rental : rentals) {
            Penalty penalty = getLateReturnPenalty(rental, penaltyType);
            if (penalty != null) {
                penalties.add(penalty);
                penaltyRepo.save(penalty);
            }
        }
        return penalties;
    }

    public List<Penalty> getPenaltiesByCustomerId(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty.");
        }
        return penaltyRepo.findByCustomerId(customerId);
    }

    public List<Penalty> getAllPenalties() {
        return penaltyRepo.findAll();
    }
}