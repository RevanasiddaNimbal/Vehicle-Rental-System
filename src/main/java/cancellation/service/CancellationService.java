package cancellation.service;

import cancellation.factory.CancellationStrategyFactory;
import cancellation.model.CancellationRecord;
import cancellation.model.PolicyType;
import cancellation.repository.CancellationRepo;
import cancellation.strategy.CancellationStrategy;
import exception.ResourceNotFoundException;
import payment.facade.PaymentFacade;
import rental.model.Rental;
import rental.model.RentalStatus;
import rental.service.RentalService;
import util.IdGeneratorUtil;
import util.IdPrefix;
import vehicle.models.Vehicle;
import vehicle.service.VehicleService;

import java.time.LocalDateTime;
import java.util.List;

public class CancellationService {
    private final RentalService rentalService;
    private final VehicleService vehicleService;
    private final CancellationRepo cancellationRepo;
    private final PaymentFacade paymentFacade;
    private final CancellationStrategyFactory cancellationStrategyFactory;

    public CancellationService(RentalService rentalService, VehicleService vehicleService, CancellationRepo cancellationRepo, PaymentFacade paymentFacade, CancellationStrategyFactory cancellationStrategyFactory) {
        this.rentalService = rentalService;
        this.vehicleService = vehicleService;
        this.cancellationRepo = cancellationRepo;
        this.paymentFacade = paymentFacade;
        this.cancellationStrategyFactory = cancellationStrategyFactory;
    }

    public double cancelRentalByRentalId(int rentalId) {
        Rental rental = rentalService.getRentalById(rentalId);
        if (rental == null) {
            throw new ResourceNotFoundException("Rental ID " + rentalId + " not found.");
        }
        if (rental.getStatus() == RentalStatus.CANCELLED) {
            throw new IllegalStateException("Rental is already cancelled.");
        }
        if (rental.getStatus() == RentalStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed rental.");
        }

        Vehicle vehicle = vehicleService.getVehiclesById(rental.getVehicleId());
        if (vehicle == null) {
            throw new ResourceNotFoundException("Associated vehicle not found.");
        }

        CancellationStrategy strategy = cancellationStrategyFactory.getStrategy(rental);
        double refundAmount = strategy.calculateRefund(rental);
        PolicyType type = strategy.getPolicyName();

        rentalService.updateRentalStatus(rentalId, RentalStatus.CANCELLED);

        CancellationRecord record = new CancellationRecord(IdGeneratorUtil.generate(IdPrefix.CAN), rentalId, rental.getCustomerId(), rental.getVehicleId(), vehicle.getOwnerId(), type, refundAmount, LocalDateTime.now());
        cancellationRepo.save(record);

        paymentFacade.processCancellationRefund(rental.getCustomerId(), rental, refundAmount);

        return refundAmount;
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
