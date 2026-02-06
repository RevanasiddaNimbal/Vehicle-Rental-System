package Models;

public class Auto extends Vehicle {
    private int seatingCapacity;


    public Auto(String id, String brand, VehicleCategory category, double pricePerDay, int seatingCapacity) {
        super(id, brand, category, pricePerDay);
        this.seatingCapacity = seatingCapacity;
    }

    public Auto() {
        super();
    }

    ;

    @Override
    public void setStatus(VehicleStatus status) {
        super.setStatus(status);
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    @Override
    public String getVehicleType() {
        return "Auto";
    }
}
