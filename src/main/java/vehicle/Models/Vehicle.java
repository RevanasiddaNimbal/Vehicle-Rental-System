package vehicle.Models;

public abstract class Vehicle {
    private String id;
    protected String brand;
    protected Category category;
    protected double pricePerDay;
    protected Status status;

    public Vehicle(String id, String brand, Category category, double pricePerDay) {
        this.id = id;
        this.brand = brand;
        this.category = category;
        this.pricePerDay = pricePerDay;
        this.status = Status.AVAILABLE;

    }

    public String getId() {
        return id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public Integer getSeatingCapacity() {
        return null;
    }

    public Integer getEnginCapacity() {
        return null;
    }

    public FuelType getFuelType() {
        return null;
    }

}
