package exception;

public class VehicleDeleteNotAllowedException extends RuntimeException {
    public VehicleDeleteNotAllowedException(String message) {
        super(message);
    }
}