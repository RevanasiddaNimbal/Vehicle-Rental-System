package rental.stretegy;

public interface SecurityDepositStrategy {

    double calculateDeposit(String vehicleType, double finalRentalFare);
}