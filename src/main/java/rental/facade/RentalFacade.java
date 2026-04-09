package rental.facade;

import cancellation.service.CancellationService;
import payment.dto.PaymentDetails;
import payment.facade.PaymentFacade;
import payment.model.PaymentMethod;
import payment.strategy.PaymentStrategy;
import penalty.model.Penalty;
import penalty.model.PenaltyType;
import penalty.service.PenaltyService;
import rental.model.Rental;
import rental.scheduler.ReservationTimeoutManager;
import rental.service.RentalService;
import vehicle.models.Status;
import vehicle.models.Vehicle;
import vehicle.service.VehicleService;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

public class RentalFacade {

    private final RentalService rentalService;
    private final VehicleService vehicleService;
    private final PaymentFacade paymentFacade;
    private final PenaltyService penaltyService;
    private final CancellationService cancellationService;
    private final ReservationTimeoutManager timeoutManager;

    public RentalFacade(RentalService rentalService,
                        VehicleService vehicleService,
                        PaymentFacade paymentFacade,
                        PenaltyService penaltyService,
                        CancellationService cancellationService,
                        ReservationTimeoutManager timeoutManager) {

        this.rentalService = rentalService;
        this.vehicleService = vehicleService;
        this.paymentFacade = paymentFacade;
        this.penaltyService = penaltyService;
        this.cancellationService = cancellationService;
        this.timeoutManager = timeoutManager;
    }

    public boolean processBooking(String customerId, List<Rental> rentals, PaymentMethod method, PaymentDetails paymentDetails, PaymentStrategy strategy) {

        if (rentals == null || rentals.isEmpty()) {
            return false;
        }

        List<ScheduledFuture<?>> timeouts = null;

        try {

            synchronized (vehicleService) {
                for (Rental r : rentals) {
                    Vehicle v = vehicleService.getVehiclesById(r.getVehicleId());
                    if (v == null || v.getStatus() != Status.AVAILABLE) {
                        return false;
                    }
                }
                vehicleService.updateStatus(rentals, Status.RESERVED);
            }

            timeouts = scheduleTimeouts(rentals);

            boolean paymentSuccess;
            try {
                paymentSuccess = paymentFacade.processBookingPayments(customerId, rentals, strategy, paymentDetails);
            } catch (Exception e) {
                rollback(rentals, timeouts);
                return false;
            }

            if (!paymentSuccess) {
                rollback(rentals, timeouts);
                return false;
            }

            rentalService.bookRentalsAsync(rentals);

            vehicleService.updateStatus(rentals, Status.RENTED);

            cancelTimeouts(timeouts);

            return true;

        } catch (Exception e) {
            rollbackWithRetry(customerId, rentals, timeouts);
            return false;
        }
    }

    private List<ScheduledFuture<?>> scheduleTimeouts(List<Rental> rentals) {
        return rentals.stream()
                .map(r -> timeoutManager.scheduleUnlock(r.getVehicleId(), 2))
                .collect(Collectors.toList());
    }

    private void rollback(List<Rental> rentals, List<ScheduledFuture<?>> timeouts) {
        try {
            vehicleService.updateStatus(rentals, Status.AVAILABLE);
        } catch (Exception ignored) {
        }
        cancelTimeouts(timeouts);
    }

    private void rollbackWithRetry(String customerId, List<Rental> rentals, List<ScheduledFuture<?>> timeouts) {
        rollback(rentals, timeouts);
        try {
            paymentFacade.refundWithRetry(customerId, rentals, 3);
        } catch (Exception e) {
            throw new RuntimeException("Critical: refund failed after rollback");
        }
    }

    private void cancelTimeouts(List<ScheduledFuture<?>> timeouts) {
        if (timeouts != null) {
            for (ScheduledFuture<?> t : timeouts) {
                try {
                    t.cancel(false);
                } catch (Exception ignored) {
                }
            }
        }
    }

    public List<Penalty> calculateReturnPenalties(List<Rental> rentals) {
        return penaltyService.calculatePenalties(rentals, PenaltyType.LATE_RETURN);
    }

    public void processReturn(String customerId, List<Rental> rentals, List<Penalty> penalties) {
        paymentFacade.processReturnPayouts(customerId, rentals, penalties);
        rentalService.completeRentals(rentals);
        vehicleService.updateStatus(rentals, Status.AVAILABLE);
    }

    public double processCancellation(String customerId, Rental rental) {
        double refundAmount = cancellationService.cancelRentalByRentalId(rental.getId());
        vehicleService.updateStatusById(rental.getVehicleId(), Status.AVAILABLE);
        return refundAmount;
    }
}