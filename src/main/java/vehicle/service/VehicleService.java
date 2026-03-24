package vehicle.service;

import exception.DataAccessException;
import exception.ServiceException;
import exception.ValidationException;
import rental.model.Rental;
import vehicle.models.Status;
import vehicle.models.Vehicle;
import vehicle.repository.VehicleRepo;
import vehicle.validation.VehicleValidator;

import java.util.List;
import java.util.Objects;

public class VehicleService {
    private final VehicleRepo repository;
    private final VehicleValidator validator;

    public VehicleService(VehicleRepo repository) {
        this(repository, new VehicleValidator());
    }

    public VehicleService(VehicleRepo repository, VehicleValidator validator) {
        this.repository = Objects.requireNonNull(repository, "repository");
        this.validator = Objects.requireNonNull(validator, "validator");
    }

    // ===== CREATE =====
    public boolean addVehicle(Vehicle vehicle) {
        validator.validateForCreate(vehicle);
        try {
            return repository.save(vehicle);
        } catch (DataAccessException dae) {
            throw new ServiceException("Failed to add vehicle.", dae);
        }
    }

    // ===== READ =====
    public List<Vehicle> getVehiclesByStatus(Status status) {
        if (status == null) throw new ValidationException("Status is required.");
        try {
            return repository.findByStatus(status);
        } catch (DataAccessException dae) {
            throw new ServiceException("Failed to fetch vehicles by status: " + status, dae);
        }
    }

    // Keep your existing method name to avoid breaking other code
    public Vehicle getVehiclesById(String id) {
        if (id == null || id.isBlank()) throw new ValidationException("Vehicle id is required.");
        try {
            return repository.findById(id);
        } catch (DataAccessException dae) {
            throw new ServiceException("Failed to fetch vehicle id=" + id, dae);
        }
    }

    // Alias (optional). If other code doesn't use it, it's still fine to have.
    public Vehicle getVehicleById(String id) {
        return getVehiclesById(id);
    }

    public List<Vehicle> getVehiclesByOwnerId(String ownerId) {
        if (ownerId == null || ownerId.isBlank()) throw new ValidationException("Owner id is required.");
        try {
            return repository.findByOwnerId(ownerId);
        } catch (DataAccessException dae) {
            throw new ServiceException("Failed to fetch vehicles for ownerId=" + ownerId, dae);
        }
    }

    public List<Vehicle> getVehicles() {
        try {
            return repository.findAll();
        } catch (DataAccessException dae) {
            throw new ServiceException("Failed to fetch vehicles.", dae);
        }
    }

    // New clean name (optional)
    public List<Vehicle> getAllVehicles() {
        return getVehicles();
    }

    // ===== UPDATE =====
    public boolean updateVehicle(Vehicle vehicle) {
        validator.validateForUpdate(vehicle);
        try {
            return repository.update(vehicle);
        } catch (DataAccessException dae) {
            throw new ServiceException("Failed to update vehicle id=" + vehicle.getId(), dae);
        }
    }

    public boolean updateStatusById(String id, Status status) {
        if (id == null || id.isBlank()) throw new ValidationException("Vehicle id is required.");
        if (status == null) throw new ValidationException("Status is required.");

        try {
            Vehicle vehicle = repository.findById(id);
            if (vehicle == null) return false;
            vehicle.setStatus(status);
            return repository.update(vehicle);
        } catch (DataAccessException dae) {
            throw new ServiceException("Failed to update vehicle status for id=" + id, dae);
        }
    }

    public void updateStatus(List<Rental> rentals, Status status) {
        if (rentals == null || rentals.isEmpty()) return;
        if (status == null) throw new ValidationException("Status is required.");

        for (Rental rental : rentals) {
            if (rental == null) continue;
            updateStatusById(rental.getVehicleId(), status);
        }
    }

    // ===== DELETE =====
    public boolean deleteVehicle(Vehicle vehicle) {
        if (vehicle == null || vehicle.getId() == null || vehicle.getId().isBlank()) {
            throw new ValidationException("Vehicle id is required for delete.");
        }
        try {
            return repository.deleteById(vehicle.getId());
        } catch (DataAccessException dae) {
            throw new ServiceException("Failed to delete vehicle id=" + vehicle.getId(), dae);
        }
    }

    // Keep your old behavior if controller uses deleteById directly
    public boolean deleteVehicleById(String id) {
        if (id == null || id.isBlank()) throw new ValidationException("Vehicle id is required for delete.");
        try {
            return repository.deleteById(id);
        } catch (DataAccessException dae) {
            throw new ServiceException("Failed to delete vehicle id=" + id, dae);
        }
    }
}