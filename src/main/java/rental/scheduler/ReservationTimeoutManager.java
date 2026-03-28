package rental.scheduler;

import vehicle.models.Status;
import vehicle.models.Vehicle;
import vehicle.service.VehicleService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ReservationTimeoutManager {
    private final VehicleService vehicleService;
    private final ScheduledExecutorService scheduler;

    public ReservationTimeoutManager(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "Reservation-Timeout-Thread");
            t.setDaemon(true);
            return t;
        });
    }

    public ScheduledFuture<?> scheduleUnlock(String vehicleId, int timeoutMinutes) {
        return scheduler.schedule(() -> {
            try {
                Vehicle checkVehicle = vehicleService.getVehiclesById(vehicleId);
                if (checkVehicle != null && checkVehicle.getStatus() == Status.RESERVED) {
                    vehicleService.updateStatusById(vehicleId, Status.AVAILABLE);
                }
            } catch (Exception ignored) {
            }
        }, timeoutMinutes, TimeUnit.MINUTES);
    }

    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}