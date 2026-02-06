package Models;

public abstract class Vehicle {
    protected String id;
    protected String brand;
    protected VehicleCategory category;
    protected double pricePerDay;
    protected VehicleStatus status;

    public Vehicle(String id, String brand, VehicleCategory category, double pricePerDay) {
        this.id = id;
        this.brand = brand;
        this.category = category;
        this.pricePerDay = pricePerDay;
        this.status = VehicleStatus.AVAILABLE;

    }

    public Vehicle() {
    }

    ;

    public abstract String getVehicleType();

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCategory(VehicleCategory category) {
        this.category = category;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }


    public String getId() {
        return id;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + " | " + brand + " | " + category + " | ₹" + pricePerDay + " | " + status;
    }

}
