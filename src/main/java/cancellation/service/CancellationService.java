package cancellation.service;

import cancellation.factory.CancellationStrategyFactory;
import cancellation.model.CancellationRecord;
import cancellation.model.PolicyType;
import cancellation.repository.CancellationRepo;
import cancellation.stretegy.CancellationStrategy;
import rental.model.Rental;
import rental.model.RentalStatus;
import rental.service.RentalService;
import util.IdGeneratorUtil;
import vehicle.models.Vehicle;
import vehicle.service.VehicleService;

import java.time.LocalDateTime;
import java.util.List;

public class CancellationService {
    private final RentalService rentalService;
    private final VehicleService vehicleService;
    private final CancellationRepo cancellationRepo;

    public CancellationService(RentalService rentalService, VehicleService vehicleService, CancellationRepo cancellationRepo) {
        this.rentalService = rentalService;
        this.vehicleService = vehicleService;
        this.cancellationRepo = cancellationRepo;
    }

    public void cancelRentalByRentalId(int rentalId) {
        Rental rental = rentalService.getRentalById(rentalId);
        if (rental == null) {
            System.out.println("Invalid rental Id");
            return;
        }
        if (rental.getStatus().equals(RentalStatus.CANCELLED)) {
            System.out.println("Rental is already cancelled");
            return;
        }

        CancellationStrategy strategy = CancellationStrategyFactory.getStrategy(rental);
        double refundAmount = strategy.calculateRefund(rental);
        PolicyType type = strategy.getPolicyName();

        rentalService.updateRentalStatus(rentalId, RentalStatus.CANCELLED);

        Vehicle vehicle = vehicleService.getVehiclesById(rental.getVehicleId());

        CancellationRecord record = new CancellationRecord(IdGeneratorUtil.generateCancellationId(), rentalId, rental.getCustomerId(), rental.getVehicleId(), vehicle.getOwnerId(), type, refundAmount, LocalDateTime.now());
        cancellationRepo.save(record);
    }

    public List<CancellationRecord> getAllCancellationRecords() {
        return cancellationRepo.findAll();
    }

    public List<CancellationRecord> getAllCancellationRecordsByOwnerId(String ownerId) {
        return cancellationRepo.findByOwnerId(ownerId);
    }

    public List<CancellationRecord> getAllCancellationRecordsByCustomerId(String customerId) {
        return cancellationRepo.findByCustomerId(customerId);
    }
}
