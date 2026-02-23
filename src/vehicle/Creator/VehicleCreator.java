package vehicle.Creator;

import vehicle.Models.Vehicle;

import java.util.Scanner;

public interface VehicleCreator {
    Vehicle createVehicle(String id, Scanner scanner);
}
