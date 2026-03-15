package vehicle.models;

public class Auto extends Vehicle {
    private int seatingCapacity;

    public Auto(String id, String brand, Category category, double pricePerDay, int seatingCapacity, Status status, String ownerId) {
        super(id, brand, category, pricePerDay, status, ownerId);
        this.seatingCapacity = seatingCapacity;
        vehicle_type = "Auto";
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    @Override
    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }
};
