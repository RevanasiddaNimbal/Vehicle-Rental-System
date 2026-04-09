package rental.strategy;

public interface SecurityDepositStrategy {

    double calculateDeposit(String vehicleType, double finalRentalFare);
}