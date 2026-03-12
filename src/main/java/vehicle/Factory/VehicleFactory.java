package vehicle.Factory;

import vehicle.Models.*;

public class VehicleFactory {
    public static Vehicle createVehicle(String type, String id, String brand, Category category, double pricePerDay, Status status, int engineCapacity, int seatingCapacity, FuelType fuelType) {
        switch (type.toLowerCase()) {
            case "bike" -> {
                return new Bike(id, brand, category, pricePerDay, fuelType, engineCapacity, status);
            }
            case "car" -> {
                return new Car(id, brand, category, pricePerDay, seatingCapacity, fuelType, status);
            }
            case "auto" -> {
                return new Auto(id, brand, category, pricePerDay, seatingCapacity, status);
            }
            default -> {
                return null;
            }
        }
    }
}
