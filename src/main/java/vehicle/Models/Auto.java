package vehicle.Models;

public class Auto extends Vehicle {
    private int seatingCapacity;

    public Auto(String id, String brand, Category category, double pricePerDay, int seatingCapacity) {
        super(id, brand, category, pricePerDay);
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
