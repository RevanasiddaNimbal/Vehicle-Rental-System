package vehicle.validation;

import exception.ValidationException;
import vehicle.models.Vehicle;

public class VehicleValidator {

    public void validateForCreate(Vehicle vehicle) {
        validateCommon(vehicle);
        if (vehicle.getOwnerId() == null || vehicle.getOwnerId().isBlank()) {
            throw new ValidationException("OwnerId is required.");
        }
    }

    public void validateForUpdate(Vehicle vehicle) {
        validateCommon(vehicle);
        if (vehicle.getId() == null || vehicle.getId().isBlank()) {
            throw new ValidationException("Vehicle id is required for update.");
        }
    }

    private void validateCommon(Vehicle vehicle) {
        if (vehicle == null) {
            throw new ValidationException("Vehicle cannot be null.");
        }
        if (vehicle.getBrand() == null || vehicle.getBrand().isBlank()) {
            throw new ValidationException("Brand is required.");
        }
        if (vehicle.getCategory() == null) {
            throw new ValidationException("Category is required.");
        }
        if (vehicle.getStatus() == null) {
            throw new ValidationException("Status is required.");
        }
        if (vehicle.getPricePerDay() <= 0) {
            throw new ValidationException("Price per day must be greater than 0.");
        }
    }
}