package vehicle.models;

public class Car extends Vehicle {
    private FuelType fuel;
    private int seatingCapacity;

    public Car(String id, String brand, Category category, double pricePerDay, int seatingCapacity, FuelType fuel, Status status, String ownerId) {
        super(id, brand, category, pricePerDay, status, ownerId);
        this.fuel = fuel;
        this.seatingCapacity = seatingCapacity;
        vehicle_type = VehicleType.CAR.name();
    }

    public void setFuel(FuelType fuel) {
        this.fuel = fuel;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    @Override
    public FuelType getFuelType() {
        return fuel;
    }

    @Override
    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

}
