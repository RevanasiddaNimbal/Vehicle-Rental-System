package util;

public class IdGeneratorUtil {
    private static int vehicleCounter = 1;
    private static int ownerCounter = 1;
    private static int customerCounter = 1;
    private static int rentalCounter = 1;
    private static int penaltyCounter = 1;
    private static int cancellationCounter = 1;
    private static int walletCounter = 1;


    public static String generateVehicleId() {
        return String.format("VEH-%03d", vehicleCounter++);
    }

    public static String generateVehicleOwnerId() {
        return String.format("OWN-%03d", ownerCounter++);
    }

    public static String generateCustomerId() {
        return String.format("CUST-%03d", customerCounter++);
    }

    public static int generateRentalId() {
        return rentalCounter++;
    }

    public static String generatePenaltyId() {
        return String.format("PEN-%03d", penaltyCounter++);
    }

    public static String generateCancellationId() {
        return String.format("CAN-%03d", cancellationCounter++);
    }

    public static String generateWalletId() {
        return String.format("WAL-%03d", walletCounter++);
    }


}
