package vehicle.policy;

import exception.ValidationException;
import vehicle.models.Category;
import vehicle.models.VehicleType;

public final class CategoryPolicy {
    public static Category[] allowedCategories(VehicleType type) {
        if (type == null) throw new ValidationException("Vehicle type is required.");

        return switch (type) {
            case BIKE -> new Category[]{Category.SPORTS, Category.CRUISER};
            case CAR -> new Category[]{Category.SUV, Category.SEDAN, Category.HATCHBACK};
            case AUTO -> new Category[]{Category.ELECTRIC_AUTO, Category.CNG_AUTO};
        };
    }
}