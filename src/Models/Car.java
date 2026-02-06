package Models;

public class Car extends Vehicle {

    private FuelType fuel;
    private int seatingCapacity;

    public Car(String id, String brand, VehicleCategory category, double pricePerDay, int seatingCapacity, FuelType fuel) {
        super(id, brand, category, pricePerDay);
        this.fuel = fuel;
        this.seatingCapacity = seatingCapacity;
    }

    public Car() {
        super();
    }

    public void setFuel(FuelType fuel) {
        this.fuel = fuel;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    @Override
    public void setStatus(VehicleStatus status) {
        super.setStatus(status);
    }

    @Override
    public String getVehicleType() {
        return "Car";
    }
}
