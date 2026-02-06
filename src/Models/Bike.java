package Models;

public class Bike extends Vehicle {
    private int enginCapacity;
    protected FuelType fuel;

    public Bike(String id, String brand, VehicleCategory category, double pricePerDay, FuelType fuel, int enginCapacity) {
        super(id, brand, category, pricePerDay);
        this.enginCapacity = enginCapacity;
        this.fuel = fuel;
    }

    public Bike() {
        super();
    }

    public void setEnginCapacity(int enginCapacity) {
        this.enginCapacity = enginCapacity;
    }

    public void setFuel(FuelType fuel) {
        this.fuel = fuel;
    }

    public FuelType getFuel() {
        return fuel;
    }

    public int getEnginCapacity() {
        return enginCapacity;
    }

    @Override
    public void setStatus(VehicleStatus status) {
        super.setStatus(status);
    }

    @Override
    public String getVehicleType() {
        return "Bike";
    }
}
