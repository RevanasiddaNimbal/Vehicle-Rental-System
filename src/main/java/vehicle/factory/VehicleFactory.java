package vehicle.factory;

import vehicle.models.*;

public class VehicleFactory {
    public static Vehicle createVehicle(String type, String id, String brand, Category category, double pricePerDay, Status status, int engineCapacity, int seatingCapacity, FuelType fuelType, String ownerId) {
        switch (type.toLowerCase()) {
            case "bike" -> {
                return new Bike(id, brand, category, pricePerDay, fuelType, engineCapacity, status, ownerId);
            }
            case "car" -> {
                return new Car(id, brand, category, pricePerDay, seatingCapacity, fuelType, status, ownerId);
            }
            case "auto" -> {
                return new Auto(id, brand, category, pricePerDay, seatingCapacity, status, ownerId);
            }
            default -> {
                return null;
            }
        }
    }
}
