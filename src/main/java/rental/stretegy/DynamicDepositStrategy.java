package rental.stretegy;

public class DynamicDepositStrategy implements SecurityDepositStrategy {

    @Override
    public double calculateDeposit(String vehicleType, double finalRentalFare) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("VehicleType cannot be null for deposit calculation.");
        }

        switch (vehicleType) {
            case "CAR":
                return 5000.0 + (finalRentalFare * 0.10);
            case "BIKE":
                return 1000.0;
            case "AUTO":
                return 2500.0 + (finalRentalFare * 0.05);
            default:
                return 2000.0;
        }
    }
}
