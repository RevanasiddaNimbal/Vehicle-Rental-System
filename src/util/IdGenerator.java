package util;

public class IdGenerator {
    private static int counter = 1;

    public static String generateVehicleId() {
        return String.format("VEH-%03d", counter++);
    }
}
