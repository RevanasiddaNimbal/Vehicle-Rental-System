package vehicle.creator;

import vehicle.models.Vehicle;

import java.util.Scanner;

public interface VehicleCreator {
    Vehicle createVehicle(String id, Scanner scanner, String ownerId);
}
