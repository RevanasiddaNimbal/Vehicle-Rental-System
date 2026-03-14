package vehicle.Models;

public class Bike extends Vehicle {
    private int enginCapacity;
    protected FuelType fuel;

    public Bike(String id, String brand, Category category, double pricePerDay, FuelType fuel, int enginCapacity, Status status, String ownerId) {
        super(id, brand, category, pricePerDay, status, ownerId);
        this.enginCapacity = enginCapacity;
        this.fuel = fuel;
        vehicle_type = "Bike";
    }

    public void setEnginCapacity(int enginCapacity) {
        this.enginCapacity = enginCapacity;
    }

    public void setFuel(FuelType fuel) {
        this.fuel = fuel;
    }

    @Override
    public FuelType getFuelType() {
        return fuel;
    }

    @Override
    public Integer getEnginCapacity() {
        return enginCapacity;
    }
}
