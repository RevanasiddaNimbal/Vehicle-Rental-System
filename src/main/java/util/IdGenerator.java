package util;

public class IdGenerator {
    private static int vehicleCounter = 1;
    private static int ownerCounter = 1;

    public static String generateVehicleId() {
        return String.format("VEH-%03d", vehicleCounter++);
    }

    public static String generateVehicleOwnerId() {
        return String.format("OWN-%03d", ownerCounter++);
    }
}
